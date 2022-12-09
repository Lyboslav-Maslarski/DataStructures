package vaccopsjava;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        VaccOps vaccOps = new VaccOps();

        Doctor doctor1 = new Doctor("Pesho", 10);
        Doctor doctor2 = new Doctor("Gosho", 100);
        Doctor doctor3 = new Doctor("Tosho", 1);
        Doctor doctor4 = new Doctor("Momchil", 1);
        Doctor doctor5 = new Doctor("Krasi", 1);

        Patient patient1 = new Patient("Lora", 168, 25, "Sofia");
        Patient patient2 = new Patient("Nora", 168, 14, "Sofia");
        Patient patient3 = new Patient("Vqra", 175, 22, "Pleven");
        Patient patient4 = new Patient("Magi", 180, 39, "Burgas");
        Patient patient5 = new Patient("Viki", 160, 65, "Pleven");


        System.out.println("=====================");
        vaccOps.addDoctor(doctor1);
        vaccOps.addDoctor(doctor2);
        vaccOps.addDoctor(doctor3);
        vaccOps.addPatient(doctor1,patient1);
        vaccOps.addPatient(doctor1,patient2);
        vaccOps.addPatient(doctor2,patient3);
        vaccOps.addPatient(doctor2,patient4);
        vaccOps.addPatient(doctor3,patient5);


        Collection<Patient> sorted = vaccOps.getPatientsSortedByDoctorsPopularityAscThenByHeightDescThenByAge();
        System.out.println("=====================");
    }
}
