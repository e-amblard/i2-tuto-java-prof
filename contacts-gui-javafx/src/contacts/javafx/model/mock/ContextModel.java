package contacts.javafx.model.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.factory.Mappers;

import contacts.javafx.data.mapper.IMapper;
import contacts.javafx.model.IContextModel;

public class ContextModel implements IContextModel {

  // Champs

  private final List<Object> beans = new ArrayList<>();

  private final Donnees donnees;

  private final IMapper mapper;

  // Constructeur

  public ContextModel() {
    donnees = new Donnees();
    mapper = Mappers.getMapper(IMapper.class);
  }

  // Actions

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getModel(Class<T> type) {

    // Recherche dans la liste
    Object bean = null;
    for (Object obj : beans) {
      if (type.isAssignableFrom(obj.getClass())) {
        bean = obj;
        break;
      }
    }

    // Si pas trouvé dans la liste
    if (bean == null) {
      try {

        // Détermine le type à instancier
        Class<T> typeImpl;
        String nomImpl = type.getSimpleName();
        if (nomImpl.charAt(0) == 'I') {
          nomImpl = nomImpl.substring(1);
        }
        String nomPackage = this.getClass().getPackage().getName();
        nomImpl = nomPackage + "." + nomImpl;
        typeImpl = (Class<T>) Class.forName(nomImpl);
        Constructor<T> constructor = typeImpl.getConstructor(new Class[] {});

        // Instancie l'objet
        bean = constructor.newInstance(new Object[] {});

        // Injecte les dépendances
        for (Method method : typeImpl.getDeclaredMethods()) {
          if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
            Class<?> typeParam = method.getParameterTypes()[0];
            if (method.getName().startsWith("setModel")) {
              method.invoke(bean, getModel(typeParam));
            } else if (typeParam.isAssignableFrom(Donnees.class)) {
              method.invoke(bean, donnees);
            } else if (typeParam.isAssignableFrom(IMapper.class)) {
              method.invoke(bean, mapper);
            }
          }
        }

        // Ajoute l'objet à la liste
        beans.add(bean);

      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return (T) bean;
  }

}
