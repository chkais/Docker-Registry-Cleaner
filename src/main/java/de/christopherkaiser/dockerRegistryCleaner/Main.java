package de.christopherkaiser.dockerRegistryCleaner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.christopherkaiser.dockerRegistryCleaner.config.GuiceModule;

import java.io.IOException;

public class Main {


    private static Injector injector;
    private static RegistryCleaner registryCleaner;

    public static void main(String[] args) throws IOException {
        System.out.println("‼️‼️‼️ Starting Registry CleanUp ‼️‼️‼️");
        injector = Guice.createInjector(new GuiceModule());
        registryCleaner = injector.getInstance(RegistryCleaner.class);
        registryCleaner.performCleanUp();
        System.out.println("‼️‼️‼️ Registry CleanUp finished! Please perform a garbage collection on the registry ‼️‼️‼️");
    }

}
