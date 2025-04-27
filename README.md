<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Rapport - Mini-Framework d'Injection de Dépendances</title>
</head>
<body>
    <h1>Rapport : Implémentation d'un mini-framework d'injection de dépendances (DI) en Java</h1>

    <h2>Introduction</h2>
    <p>Dans ce projet, nous avons conçu un <strong>mini-framework</strong> d’injection de dépendances inspiré de <strong>Spring</strong>.<br>
    L'objectif principal est de simplifier la gestion des dépendances entre les objets via trois techniques d’injection :</p>
    <ul>
        <li>Injection par constructeur</li>
        <li>Injection par setter</li>
        <li>Injection par attribut</li>
    </ul>
    <p>Nous utilisons également <strong>la réflexion</strong> pour injecter automatiquement les dépendances au moment de la création des objets.</p>
    <p>Le framework propose :</p>
    <ul>
        <li>L'injection via un fichier <strong>XML</strong> (en utilisant JAX-B),</li>
        <li>L'injection via <strong>annotations</strong>,</li>
        <li>Et surtout, <strong>l'injection dynamique</strong> par <strong>réflexion</strong>.</li>
    </ul>

    <h2>Partie 1 : Concepts de base de l'injection de dépendances</h2>

    <h3>1. Création de l'interface <code>IDao</code></h3>
    <pre><code>package net.adam.dao;

public interface IDao {
    String getData();
}</code></pre>

    <h3>2. Implémentation de <code>IDao</code></h3>
    <pre><code>package net.adam.dao;

public class DaoImpl implements IDao {

    @Override
    public String getData() {
        return "Données récupérées depuis la source de données";
    }
}</code></pre>

    <h3>3. Création de l'interface <code>IMetier</code></h3>
    <pre><code>package net.adam.metier;

public interface IMetier {
    void calcul();
}</code></pre>

    <h3>4. Implémentation de <code>IMetier</code></h3>
    <pre><code>package net.adam.metier;

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
}</code></pre>

    <h2>Partie 2 : Développement du Mini-Framework</h2>

    <h3>5. Création du <code>BeanFactory</code></h3>
    <pre><code>package net.adam.config;

import net.adam.dao.IDao;
import net.adam.dao.DaoImpl;

import java.lang.reflect.Constructor;

public class BeanFactory {

    public static &lt;T&gt; T createBean(Class&lt;T&gt; clazz) throws Exception {
        Constructor&lt;?&gt;[] constructors = clazz.getDeclaredConstructors();
        
        for (Constructor&lt;?&gt; constructor : constructors) {
            if (constructor.getParameterCount() &gt; 0) {
                Class&lt;?&gt;[] parameterTypes = constructor.getParameterTypes();
                
                if (parameterTypes.length == 1 && parameterTypes[0].equals(IDao.class)) {
                    IDao dao = new DaoImpl();
                    return (T) constructor.newInstance(dao);
                }
            }
        }
        return clazz.getDeclaredConstructor().newInstance();
    }
}</code></pre>

    <h2>Partie 3 : Test du Framework</h2>
    <pre><code>package net.adam.annotation;

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
}</code></pre>

    <h2>Partie 4 : Résumé du fonctionnement</h2>
    <ul>
        <li><strong>Création d'objets</strong> : <code>BeanFactory</code> utilise la réflexion pour détecter les constructeurs adaptés.</li>
        <li><strong>Injection des dépendances</strong> : Si un constructeur exige une dépendance (<code>IDao</code>), elle est instanciée et injectée automatiquement.</li>
        <li><strong>Exécution</strong> : Une fois l'objet prêt, la logique métier (<code>calcul()</code>) est appelée.</li>
    </ul>

    <h2>Conclusion</h2>
    <p>Ce mini-framework montre comment <strong>gérer l'injection de dépendances en Java</strong> sans utiliser des outils lourds comme Spring.<br>
    Grâce à <strong>la réflexion</strong>, nous avons automatisé la création et l'injection des objets, ce qui offre une meilleure modularité et une séparation claire des responsabilités.<br>
    Même s’il reste simple comparé à des solutions complètes, ce projet met en lumière <strong>les principes fondamentaux</strong> du développement orienté objets et de la conception flexible.</p>

</body>
</html>
