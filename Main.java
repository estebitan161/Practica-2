import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.*;



class Main {
    static List<Student>estudiantes;
    public static void main(String[] args) throws IOException {
        cargarArchivo();
        mostrarEstudiantesPorCarrera();
        mostrarEstudiantesMujeres();
        mostrarEstudiantesHombres();
        EstMejorNotaCarrera();
        EstudianteMejorNota();
        promNotaCarrera();


    }
    static void cargarArchivo() throws IOException{
        Pattern pattern =Pattern.compile(",");
        String filename= "student-scores.csv";

        try(Stream<String> lines = Files.lines(Path.of(filename))){
            estudiantes=lines.skip(1).map(line->{
                String[]var2=pattern.split(line);
                return new Student(var2[1], var2[2], (var2[4]), var2[9], Integer.parseInt(var2[11]));
            }).collect(Collectors.toList());
            estudiantes.forEach(System.out::println);
        }
    }

    static void mostrarEstudiantesPorCarrera(){
        System.out.printf("%nEmpleados por Carrera:%n");
        Map<String, List<Student>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Student::getCareer));
        agrupadoPorCarrera.forEach(
                (carrera, estudiantesPorCarrera) ->
                {
                    System.out.println("-".repeat(110));
                    System.out.printf("%n" + carrera + "%n");
                    estudiantesPorCarrera.forEach(
                            estudiante -> System.out.printf(" %s%n", estudiante));
                }
        );
    }

    static void mostrarEstudiantesMujeres() {
        System.out.printf("%nEstudiantes Mujeres:%n");
        estudiantes.stream()
                .filter(estudiante -> "female".equals(estudiante.getGender()))
                .forEach(estudiante -> System.out.printf(" %s%n", estudiante));
    }

    static void mostrarEstudiantesHombres() {
        System.out.printf("%nEstudiantes Hombres:%n");
        estudiantes.stream()
                .filter(estudiante -> "male".equals(estudiante.getGender()))
                .forEach(estudiante -> System.out.printf(" %s%n", estudiante));
    }

    static void EstMejorNotaCarrera(){
        Function<Student, Integer> porNota = Student::getMath_score;
        Comparator<Student> NotaDescendete =
                Comparator.comparing(porNota);
        System.out.printf("%nEstudiantes Por Carrera: %n");
        Map<String, List<Student>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Student::getCareer));
        agrupadoPorCarrera.forEach(
                (carrera, estudiantesEnCarrera) ->
                {
                    System.out.print(carrera+": ");
                    Student estudianteMas=estudiantesEnCarrera.stream().sorted(NotaDescendete.reversed()).findFirst().get();
                    System.out.println(estudianteMas.toString());
                }
        );
    }

    static void EstudianteMejorNota(){
        Function<Student, Integer> porNota = Student::getMath_score;
        Comparator<Student> NotaDescendete =
                Comparator.comparing(porNota);
        Student estudianteMas= estudiantes.stream().max(NotaDescendete).get();
        System.out.printf(
                "%nEstudiante Con Mejor Nota:%n%s %n",
                estudianteMas.toString());
    }

    static void promNotaCarrera(){
        Map<String, List<Student>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Student::getCareer));
        System.out.println("\nPromedio de Notas por Carrera:");
        agrupadoPorCarrera.forEach((carrera, estudiantesPorCarrera)->
        {
            System.out.print(carrera+": ");
            System.out.println(estudiantesPorCarrera
                    .stream()
                    .mapToDouble(Student::getMath_score)
                    .average()
                    .getAsDouble());
        });
    }
}
