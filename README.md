# Rapport : Implémentation d'un mini-framework d'injection de dépendances (DI) en Java

## Introduction

Dans ce projet, nous avons conçu un **mini-framework** d’injection de dépendances inspiré de **Spring**.  
L'objectif principal est de simplifier la gestion des dépendances entre les objets via trois techniques d’injection :

- Injection par constructeur
- Injection par setter
- Injection par attribut

Nous utilisons également **la réflexion** pour injecter automatiquement les dépendances au moment de la création des objets.

Le framework propose :

- L'injection via un fichier **XML** (en utilisant JAX-B),
- L'injection via **annotations**,
- Et surtout, **l'injection dynamique** par **réflexion**.

## Partie 1 : Concepts de base de l'injection de dépendances

### 1. Création de l'interface `IDao`

```java
package net.adam.dao;

public interface IDao {
    String getData();
}
```

### 2. Implémentation de `IDao`

```java
package net.adam.dao;

public class DaoImpl implements IDao {

    @Override
    public String getData() {
        return "Données récupérées depuis la source de données";
    }
}
```

### 3. Création de l'interface `IMetier`

```java
package net.adam.metier;

public interface IMetier {
    void calcul();
}
```

### 4. Implémentation de `IMetier`

```java
package net.adam.metier;

import net.adam.dao.IDao;

public class MetierImpl implements IMetier {

    private IDao dao;

    // Injection via le constructeur
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public void calcul() {
        System.out.println("Traitement métier avec les données : " + dao.getData());
    }
}
```

## Partie 2 : Développement du Mini-Framework

### 5. Création du `BeanFactory`

```java
package net.adam.config;

import net.adam.dao.IDao;
import net.adam.dao.DaoImpl;

import java.lang.reflect.Constructor;

public class BeanFactory {

    public static <T> T createBean(Class<T> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() > 0) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                
                if (parameterTypes.length == 1 && parameterTypes[0].equals(IDao.class)) {
                    IDao dao = new DaoImpl();
                    return (T) constructor.newInstance(dao);
                }
            }
        }
        return clazz.getDeclaredConstructor().newInstance();
    }
}
```

## Partie 3 : Test du Framework

```java
package net.adam.annotation;

import net.adam.config.BeanFactory;
import net.adam.metier.IMetier;
import net.adam.metier.MetierImpl;

public class Main {
    public static void main(String[] args) {
        try {
            IMetier metier = BeanFactory.createBean(MetierImpl.class);
            metier.calcul();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Partie 4 : Résumé du fonctionnement

- **Création d'objets** : `BeanFactory` utilise la réflexion pour détecter les constructeurs adaptés.
- **Injection des dépendances** : Si un constructeur exige une dépendance (`IDao`), elle est instanciée et injectée automatiquement.
- **Exécution** : Une fois l'objet prêt, la logique métier (`calcul()`) est appelée.

## Conclusion

Ce mini-framework montre comment **gérer l'injection de dépendances en Java** sans utiliser des outils lourds comme Spring.  
Grâce à **la réflexion**, nous avons automatisé la création et l'injection des objets, ce qui offre une meilleure modularité et une séparation claire des responsabilités.  
Même s’il reste simple comparé à des solutions complètes, ce projet met en lumière **les principes fondamentaux** du développement orienté objets et de la conception flexible.
