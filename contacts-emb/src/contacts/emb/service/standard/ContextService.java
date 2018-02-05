package contacts.emb.service.standard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import contacts.commun.service.IContextService;
import contacts.emb.dao.IContextDao;
import contacts.emb.dao.IManagerTransaction;
import contacts.emb.data.mapper.IMapper;
import contacts.emb.service.util.IManagerSecurite;
import contacts.emb.service.util.ManagerSecurite;


public class ContextService implements IContextService  {
	
	
	// Champs
	
	private final List<Object>	beans = new ArrayList<>();
	
	private final IMapper		mapper = IMapper.INSTANCE;
	
	private IContextDao			contextDao;
	private IManagerTransaction	managerTransaction;
	private IManagerSecurite	managerSecurite;
	
	
	// Constructeur
	
	public ContextService( IContextDao contextDao, IManagerSecurite managerSecurite ) {
		this.contextDao = contextDao;
		managerTransaction = contextDao.getDao( IManagerTransaction.class );
		this.managerSecurite = managerSecurite;
	}
	
	public ContextService( IContextDao contextDao ) {
		this( contextDao, new ManagerSecurite() );
	}
	
	
	// Actions
	
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getService( Class<T> type ) {
		
		// Recherche dans la liste
		Object bean = null;
		for ( Object obj : beans ) {
			if ( type.isAssignableFrom( obj.getClass() ) ) {
				bean = obj;
				break;
			}
		}
		
		// Si pas trouvé dans la liste
		if ( bean == null ) {
			try {

				// Détermine le type à instancier
				Class<T> typeImpl;
				String nomImpl = type.getSimpleName().substring(1);
				String nomPackage = this.getClass().getPackage().getName();
				nomImpl = nomPackage + "." + nomImpl;
				typeImpl =  (Class<T>) Class.forName( nomImpl );
				Constructor<T> constructor = typeImpl.getConstructor(new Class[] {});

				// Instancie l'objet
				bean = constructor.newInstance( new Object[] {} ) ;

				// Injecte les dépendances
				for( Method method : typeImpl.getDeclaredMethods() ) {
					if ( method.getName().startsWith("set") 
							&& method.getParameterCount() == 1 )  {
						Class<?> typeParam = method.getParameterTypes()[0];
						if ( method.getName().startsWith( "setService" ) ) {
							method.invoke( bean, getService(typeParam) );
						} else if ( method.getName().startsWith( "setDao" ) ) {
							method.invoke( bean, contextDao.getDao( typeParam ) );
						} else if ( typeParam.isAssignableFrom( IMapper.class  ) ) {
							method.invoke( bean, mapper );
						} else if ( typeParam.isAssignableFrom( IManagerSecurite.class  ) ) {
							method.invoke( bean, managerSecurite );
						} else if ( typeParam.isAssignableFrom( IManagerTransaction.class  ) ) {
							method.invoke( bean, managerTransaction );
						}
					}
				}

				// Ajoute l'objet à la liste
				beans.add(bean);
						
			} catch ( RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (T) bean;
	}

}
