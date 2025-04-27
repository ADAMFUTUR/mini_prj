### Rapport sur l'implémentation de l'injection de dépendances (DI) en Java avec un mini-framework

#### Introduction

Dans le cadre de ce projet, nous allons implémenter un mini-framework d'injection de dépendances similaire à Spring. L'objectif principal est de fournir un moyen de gérer les dépendances des objets via des techniques d'injection (par constructeur, par setter, ou via des attributs) et de démontrer l'utilisation de la réflexion pour gérer l'injection des dépendances dans les objets au moment de leur création.

Nous allons également aborder les différentes façons d'injecter des dépendances, à savoir :
1. L'injection via un fichier XML (configuration via JAX-B).
2. L'injection via des annotations.
3. L'injection via la réflexion (qui est le cœur du mini-framework).

Le framework prendra en charge l'injection de dépendances de manière dynamique (par la réflexion) et statique (via des méthodes définies comme `@Autowired` ou des annotations spécifiques).

### Partie 1: Concept d'Injection des Dépendances (DI)

#### 1. Création de l'interface `IDao`

L'interface `IDao` représente un contrat pour l'accès aux données. Cette interface a une méthode `getData()` qui retourne des informations d'une source de données.

```java
package net.achraf.dao;

public interface IDao {
    String getData();
}
```

#### 2. Implémentation de l'interface `IDao`

`DaoImpl` est la classe qui implémente `IDao` et fournit une méthode pour accéder aux données. Elle simule l'accès à une base de données.

```java
package net.achraf.dao;

public class DaoImpl implements IDao {

    @Override
    public String getData() {
        return "Données récupérées depuis la source de données";
    }
}
```

#### 3. Création de l'interface `IMetier`

L'interface `IMetier` contient la méthode métier `calcul()`, qui sera responsable du traitement des données.

```java
package net.achraf.metier;

public interface IMetier {
    void calcul();
}
```

#### 4. Implémentation de l'interface `IMetier`

`MetierImpl` est la classe qui implémente `IMetier`. Elle contient une dépendance vers `IDao` et utilise cette dépendance dans la méthode `calcul()`.

```java
package net.achraf.metier;

import net.achraf.dao.IDao;

public class MetierImpl implements IMetier {

    private IDao dao;

    // Constructor pour l'injection de dépendances
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public void calcul() {
        System.out.println("Traitement métier avec les données : " + dao.getData());
    }
}
```

### Partie 2: Le Mini Framework d'Injection de Dépendances

Le mini-framework doit gérer l'injection des dépendances de manière dynamique (par réflexion) et statique (par annotations ou XML). Nous allons créer un `BeanFactory` qui permet d'injecter des dépendances dans les objets.

#### 5. Création du `BeanFactory`

Le `BeanFactory` est responsable de la création des objets et de l'injection de leurs dépendances. Nous allons utiliser la réflexion pour déterminer le constructeur des classes et injecter les dépendances nécessaires.

```java
package net.achraf.config;

import net.achraf.dao.IDao;
import net.achraf.metier.IMetier;
import net.achraf.metier.MetierImpl;

import java.lang.reflect.Constructor;

public class BeanFactory {

    // Méthode pour créer un bean (objet) et injecter ses dépendances
    public static <T> T createBean(Class<T> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        
        // Recherche d'un constructeur avec des paramètres (injection par constructeur)
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() > 0) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                
                // Si le constructeur attend un paramètre de type IDao
                if (parameterTypes.length == 1 && parameterTypes[0].equals(IDao.class)) {
                    // Créer l'instance de IDao (DaoImpl)
                    IDao dao = new DaoImpl(); 
                    
                    // Injecter la dépendance dans le constructeur
                    return (T) constructor.newInstance(dao);
                }
            }
        }
        
        // Retourner l'instance sans dépendance si aucun constructeur avec paramètres n'est trouvé
        return clazz.getDeclaredConstructor().newInstance();
    }
}
```

### Partie 3: Test de l'injection de dépendances avec `Main`

Dans la classe `Main`, nous allons tester la création d'un objet `MetierImpl` en utilisant notre `BeanFactory` et vérifier que la dépendance `IDao` est correctement injectée.

```java
package net.achraf.annotation;

import net.achraf.config.BeanFactory;
import net.achraf.metier.IMetier;
import net.achraf.metier.MetierImpl;

public class Main {
    public static void main(String[] args) {
        try {
            // Créer une instance de MetierImpl via le BeanFactory
            IMetier metier = BeanFactory.createBean(MetierImpl.class);
            // Appeler la logique métier
            metier.calcul();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Partie 4: Résumé du Fonctionnement

Le mini-framework fonctionne de la manière suivante :

1. **Création de l'objet** : Le `BeanFactory` utilise la réflexion pour déterminer quel constructeur utiliser pour créer l'objet.
2. **Injection des dépendances** : Lorsqu'un constructeur avec des paramètres est trouvé, le `BeanFactory` crée les objets nécessaires pour satisfaire les dépendances (par exemple, créer un `DaoImpl` et l'injecter dans un `MetierImpl`).
3. **Exécution de la logique métier** : Une fois l'objet créé et les dépendances injectées, la méthode `calcul()` est appelée sur l'objet `MetierImpl`.

### Conclusion

Ce mini-framework d'injection de dépendances montre comment gérer l'injection dynamique des dépendances via la réflexion en Java. Bien que le framework ne soit pas aussi puissant et flexible que Spring, il permet de mieux comprendre les concepts sous-jacents de l'injection de dépendances et de la gestion de la création d'objets. Ce projet démontre également l'utilisation de la réflexion pour créer des objets et injecter des dépendances de manière automatique.

