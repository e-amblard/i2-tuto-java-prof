package contacts.emb.dao.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import contacts.emb.dao.IContextDao;
import contacts.emb.dao.IManagerTransaction;


public class ContextDao implements IContextDao  {
	
	
	// Champs
	
	private final List<Object>	beans = new ArrayList<>();
	
	private DataSource			dataSource;
	
	
	// Constructeur
	
	public ContextDao( DataSource dataSource ) {
		this.dataSource = dataSource;
	}
	
	
	// Injecteurs
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setManagerTransaction( IManagerTransaction managerTransaciton ) {
		beans.add(managerTransaciton);
	}
	
	
	// Actions
	
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getDao( Class<T> type ) {
		
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
						if ( method.getName().startsWith( "setDao" ) ) {
							method.invoke( bean, getDao( typeParam ) );
						} else if ( typeParam.isAssignableFrom( DataSource.class  ) ) {
							method.invoke( bean, dataSource );
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
