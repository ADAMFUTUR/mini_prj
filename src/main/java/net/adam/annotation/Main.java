package net.adam.annotation;

import net.adam.config.BeanFactory;
import net.adam.metier.IMetier;
import net.adam.metier.MetierImpl;

public class Main {
    public static void main(String[] args) {
        try {
            // Create an instance of MetierImpl through the BeanFactory
            IMetier metier = (IMetier) BeanFactory.createBean(MetierImpl.class);
            // Call the business logic
            metier.calcul();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
