#Laboratorio 4: Parque de dinosaurios (nivel basico)

herramienetas: ocupe java 17 como lenguaje de programacion y maven como el administrador del proyecto 

intrucciones de configuracion: para poder configurar las caracteristicas se tiene que seguir la ruta
src/main/resources dentro de esa carpeta se encuentra la clase "park.properties" el cual tiene las caracteristicas del parque como por ejemplo 
- se encuentra el numero de dinosaurios, turistas, guardias el cual se pueden modficar 
-la semilla para generar la aleatoridad que es con la funcion simulation.seed=42
-se encuentra el total de pasos que se realizan con la funcion simulation.totalSteps=100
-se puede mofidicar los boletos, Spa y souvenirs 

forma de ejecucion del proyecto: para poder ejecutar el proyecto se abre su respectiva terminal y se usan mvn clean compile para que maven compile posteriormente mvn exec:java para que se pueda realizar la ejecucion y mvn test para poder testear si funciona correctamente una ves realizado esto se puede ver la informacion de los datos en output y revenues.csv que es en donde se guara todos los datos procesados 

descripcion del proyecto 
este proyectp simula un parque turistico de dinosaurios basado en java para administrar el parque, el sistema puede controlar el numero de dinosauriod, turistas, lotes y ademas simula eventos coomo el apagon de la energia electrica o el escape de un dinosaurio ademas de registrar los datos financioeron y poder almacenarlo en csv

explicacion de patrones 
 PATRON SINGLETON (ParkConfig)
 como cada propiedad esta guardada en park.properties como el costo del spa, entreda etc. y como estas propiedades se ocupan para cada proceso el sistme tendria que leerlas una por una para poder sacar la informacion desde el disco duro lo que provoca que sea un proceso muy tardado es por eso que en la clase ParkConfig se cree una instancia para poder guardar todas estas propiedades en la memoria Ram y asi el proceso sea mucho mas rapido 

 PATRON STRATEGY (SimulationEvent)

En este patron se busco una manera de poder agregar nuevos eventos ya que al ser un parque de dinosaurios puede haber muchos factores posibles para cualquier evento posible, para ello se busca poder agregar eventos nuevos sin la necesidad de tener que modifcar el codigo para poder realizar esto en Creamos una interfaz llamada SimulationEvent que tiene una única regla: todos los que la usen deben tener un método llamado execute() donde se crean clases independientes de los posibles eventos que pudieran ocurrir cada uno funcionando de manera independiente lo que significa que el SimulationEngine tenga que lanzar un desastre el motor simplemente toma un evento al azar de su lista y le dice: "No sé qué tipo de desastre eres, pero execute() hal hacer eso haces que se puedan actualizar 

autor: Carlos Alexis Morales Mentado