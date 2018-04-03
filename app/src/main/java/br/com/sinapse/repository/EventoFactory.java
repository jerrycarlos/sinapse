package br.com.sinapse.repository;

import java.util.Random;

import br.com.sinapse.model.Evento;

public class EventoFactory {
    private static String[] temas = {"Evento 1", "Evento 2", "Evento 3", "Evento 4", "Evento 5", "Evento 6"};
    private static String[] desc = {"\"Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n " +
            "Etiam eget ligula eu lectus lobortis condimentum.\n Aliquam nonummy auctor massa.\"",
            "\"Quis custodiet ipsos custodes?\"","\"Nam leo lectus, consectetur sed mi vel, porta congue erat.\"","\"Nam leo lectus, consectetur sed mi vel, porta congue erat. Proin sit amet dolor posuere, iaculis sapien sit amet, fermentum dolor.\"","\"Nulla egestas nibh augue, a bibendum sapien ullamcorper non. Aliquam volutpat turpis id gravida commodo. Ut at laoreet tortor.\"","\"Mauris tempor congue tellus in elementum. Suspendisse ullamcorper velit sit amet nisl luctus, vitae cursus lorem interdum. Phasellus tristique nisl sed ipsum imperdiet ultrices. Aliquam vitae tempus mi. Pellentesque nisl leo, maximus molestie magna.\""};

    public static Evento makeEvento() {
        return new Evento(temas[getRandomValue(0, 5)],
                desc[getRandomValue(0, 2)]);
    }

    private static int getRandomValue(int low, int high) {
        return new Random().nextInt(high - low) + low;
    }
}
