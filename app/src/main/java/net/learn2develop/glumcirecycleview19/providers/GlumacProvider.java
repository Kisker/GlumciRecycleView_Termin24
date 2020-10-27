package net.learn2develop.glumcirecycleview19.providers;

import net.learn2develop.glumcirecycleview19.model.Glumac;

import java.util.ArrayList;
import java.util.List;

public class GlumacProvider {
    private static List<Glumac> glumac = null;

    private static void init (){
        if (glumac == null){
            glumac = new ArrayList<>();
            glumac.add(new Glumac(1, "Milan", " Milanovic", "Biografija 1", "image1.jpg", "Flim1, Flim2",
                    4.3f, "1.1.2000","1.7.2030"));
            glumac.add(new Glumac(2, "Marko", " Markovic", "Biografija 2", "image2.jpg", "Flim1, Flim2",
                    3.8f, "",""));
            glumac.add(new Glumac(3, "Kris", " Kisker", "Biografija 3", "image3.jpg", "Flim1, Flim2",
                    5.0f, "",""));
        }
    }
    public static List <Glumac> getAllGlumac(){
        init();
        return glumac;
    }
    public static Glumac getGlumacById (int id){
        init();

        if (id >= 0 && id < glumac.size()) {
            return glumac.get(id);
        }
        return null;
    }
}
