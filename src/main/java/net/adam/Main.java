package net.adam;

import net.adam.config.BeanFactory;
import net.adam.metier.IMetier;
import net.adam.metier.MetierImpl;

public class Main {
    public static void main(String[] args) throws Exception {
        IMetier metier = BeanFactory.createBean(MetierImpl.class);
        metier.calcul();
    }
}
