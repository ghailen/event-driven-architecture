# event-driven-architecture

![image](https://github.com/user-attachments/assets/6e6952f2-48bc-4ce4-a16c-397db445295a)
le broker est toléré au panne 
topic ou queue comme des boites à lettre. des fils d'attente

dans les annees 2000 les applications jee utilisent souvent le jms (qui est un api)
![image](https://github.com/user-attachments/assets/2a9281c3-e55b-4a43-8edf-7e3125f4792e)
la destination peut etre soit de type topic ou queue.
il y a deux protocoles qui gere les files d'attente . (les queues et les topcis)
![image](https://github.com/user-attachments/assets/72b6fe28-d990-4bb3-b8ea-e12821d854b1)

queue => one to one 
le message est consomé une seule fois .
si on a deux consumers c1 et c2 qui ecoutent la meme queue , il y aura un tour de role , le premier message va etre consomer par C1 et le deuxieme message va etre consommer par C2 , aprés C1, aprés C2 ...
ça va servire lorsque qu'on va donner les taches.

Topic: si le producer envoie 4 messages , les deux consommateurs C1 et C2 chacun va recevoir 4 messages.

=======================***JMS***===========================

creer un consumer avec JMS: en utilisant spring
![image](https://github.com/user-attachments/assets/ac866d05-312d-4a18-87d6-74147290583d)
si tu veux creer avec JAVA core native , il faut passer par la session factory (dans spring toute est fait et caché par @JmsListener) ... comme c'est mentionnée dans le modele partagé en haut.

-envoyer un message  dans jms avec spring (producer):
![image](https://github.com/user-attachments/assets/9396dcbf-83bf-404d-8cf5-4216e627deb6)
jboss, weblogic ... tous fournit une implementation jms

=====================***KAFKA***=======================

sur jms (technique de push), si le producer envoie 1000 message au broker et il y a un cosumateur par exemple C1 qui est connecté et fait un subscribe au broker, ce consumer c1 va consommer tous les 1000 messages directement, (technique de push) , dés que le broker envoie le message au consomateur, il recoit un acquittement de reception et aprés il supprime le message de la file d'attente. 
=>l'inconvenient : le consumer peut etre deborder, pusiqu'il va recevoir plusierus messages au meme temps , donc si le consumer n'as de ressrouce pour stocker les messages et traiter il va finir par etre deborder.

sur kafka (technique de pull) , c'est le consumer qui decide qu'on est ce qu'il veut lire le message et avec quel debit.
les messages dans kafka peut etre reconsommer, kafka peut jouer le role de event store. sur une architectue micro service par exemple on peut stocker et generer des audits dans kafka à partir des events stocké.

![image](https://github.com/user-attachments/assets/0d215e09-f6bd-4ec7-aabd-994154713f87)

broker c'est je recoit le message et je delivre le message
les messages vont etre garder et stocker (durable) dans un topic , on peut faire des audits avec ...
-permet le real time processing : le big data (comment stocker les données d'une façon distribué dans plusieurs machine et comment utiliser ses machinss pour traiter ce grand nombre des données  , donc il y a le stockage et le traitement) 
![image](https://github.com/user-attachments/assets/14766bac-80d3-4228-9f4d-fe5e2e326317)

--------------------------------------------------------------------hors event driven---------------------------------------------
-- 
*********stockage distribué: on utilise ses bases de donnée , on enregsite ligne par ligne dans chaque machine(example ligne1 dans une machine , ligne2 dans une autre mahcine) , ou colonne par colone dans chaque machine (example colonne user dans une machine , colonne client dans une autre machine)
-MONGO DB : orienté ligne (docuement)
-CASANDRA : orienté colonne
(distribution des tables par colonne ou par ligne) , chaque sgbd a son propre stategrie de distribution.
-NEO4J : orienté graphe dans le cas des reseaux socieaux (resueaux sociaux utilise les graphes , stock les données sous forme des graphoes ) theorie de graphie , il checher les liens, les relations entre les utlisateurs. (le chemin le plus rapide ) aussi les applications de geolocalisation , (de cours chemin ) comme waze,, linkedin neo4j
-Elastic search: sgbd orienté texte , permet de creer des moteurs de recherche,qui permet de chercher par texte pour recurper les documents facilement.
-REDIS (sgbd no sql il est aussi) : ditribue les données sous forme de clé valeur , trés pratique pour creer un cache distribué , cache mémoire. (dans architecture mciroservice utilisé aussi)

****** traitement distribué:
-batch processing : je traite les données qui ont ete enregistré dans le passé, par exemple la fin de ce mois je traite tous les données de ce mois, aussi par exemple pour la jourée d'hier , je traite les données la fin de soirée de la journée d'hier., pour l'année , je traite les données de toute l'année à la fin. (Spring batch mais il n'est pas distribué)
-le spring batch est la meilleure solution, mais si on dépasse 4 heures de delai d'execution de job , ça n'est pas bien.
-map reduce: il est ancien il permet de decouper les données et les envoyers dans plusieurs machines et aprés il les recuperes et les regroupes; mais limité.
-spark est plus interessant: permert de faire le batch processing d'une maniére distribué , on a besoin d'un cluster pour travailler avec spark , c'est pour cela on voit HDFS dans le schéma .
spark pour grand volume de donneé, spring batch pour un volume moyen.
*** Stream processing:
traiter les données en temps reel (pas des données dans le passé), au fur et au mesure que je recoit les donneés , je vais le traitement , faire des analyses et prise de descion 

----------------------------------------------------------------------------------------------------------

![image](https://github.com/user-attachments/assets/1521fadf-9866-4805-a211-e36954690d8d)

![image](https://github.com/user-attachments/assets/276488e2-73a7-47a7-9a2d-35d194f8229b)

cluster : plusieurs machine.
zookeepr : coordinateur entree les differentes instances du clusrer  , puisqu'on se base sur un cluster , le broker doit etre tolerant au panne .

On suppose qu'on a un livre avec 100000 pages, et on a 10 personnes (correspond au machine). 
on suppose on a 100 partitions(crées) ,donc on va diviser les 100000 pages à 100 partitions , du coup il y a aura 1000 pages à distribué , du coup on va disrtribué les 1000 pages au 10 personnes
cahque personne va prendre 100 partition.. 
=> partitionner pour distribuer.
par exemple la personne 1 va prendre la partition 1 jursqu'au 10 , personne 2 va prenddre la partition 11 vers la partition 20 ...
maintenant si personne 1 va tomber en panne (machine) on va perdre ces partitions(les informatiosn).
mais il y a le terme replication.  les partitions de la personne 1 vont avoir une copie dans une autre personne 2 ou 3 .
replica -> creer des copies. => pour resoudre la probleme de tolerance au panne.

maintneant on suppose que la personne 1 tombe en panne. qui va demander au personne 2 de fournier la copie de la personne 1 qui est malade ou tombé en panne (machine) .
=> c'est zookeeper qui le coordinateur. c'est lui qu va déja demander de repliquer les partietions de P1 dans P2 et P3 , et de P3 dans P5 et P6 ... aprés si quelqu'un tombe on panne c'est lui qui interroge la personne concerné pour fournir la copie. et si un autre P11 qui vient d'etre ajouter , chaque autre personne va donner 2 partitions de leur partition au nouveau personne 11 et on va repartir les partitions vers le nouveau personne et les autres et aussi reassosie les copies.
=> ça c'est la scalabilité: c'est lorsqu'une nouvelle machine ou instance qui arrive il faut repartir les partitions sur les nouvelles machines. et le systeme devient plus rapide.
zookeeper qui fait ça , qui permet de gerer les differentes instance de broker.
pour conculre :
-partitionner , c'est pour distribuer.
-repliquer , c'est pour resoudre le probleme de torelance au panne.
-scalabilité: le system est scalabe expliqué dessus.
pour garantir la scalabilité , il faut avoir plusieurs partitions.  puisque si on a que 10 partitions . donc chaque personne va prendre qu'une seule partiton à la fois , si on va ajouter une nouvelle personne on pas pas l'affecter une partition , donc le systeme ne plus scalabale. => il faut plusieurs partitions.
![image](https://github.com/user-attachments/assets/7920f518-4547-4f48-9cda-f2afcd960cf9)

dans jms on parle de topic et de queue.
dans kafka on parle tjs de topic, mais dans le topic on peut implementer les deux protocoles queue et topic grace à la notion de groupe.
chaque consumer kafka doit appartenir à un groupe.
il faut savoir il le produce envoie le message, le message est envoyé vers le groupe
le groupe A va recevoir le message et le groupe B va recevoir le meme message. mais à l'intérieur de groupe il y a un seul qui va recevoir le message.
si le groupe A receoit le message il y a que le C1 qui recupere le message et dans le groupe B il ya que le C3 qui recupere le message -> c'est le topic. , c'est one to many ,  puisque les groupes sont different les deux C1 et C3 vont consommer le message
si les consomatteurs C1 et C2 sont dans le meme groupe , alors c'est qu'un seule C1 ou C2 vont consommerr puisqu'ils sont dans le meme groupe => le queue
![image](https://github.com/user-attachments/assets/79e30d8c-cfb2-4afd-9dac-31b28028f684)



*****************************************************INSTALLATION:***********************************************************
![image](https://github.com/user-attachments/assets/a084b423-053c-46dc-aef4-c6d4d3dde8dd)
![image](https://github.com/user-attachments/assets/704c93e1-4609-43b9-a737-97d2015ef78e)

il faut demarrer zookeeper d'abord sinon kafka il va pas marcher.
![image](https://github.com/user-attachments/assets/af5bafbb-d166-45e1-890a-e30243dc5069)
=> un replica et et un partition selon les parameters fournit.
pour tester :
![image](https://github.com/user-attachments/assets/925047e6-fc32-430d-a7fd-53b51a41b459)

si on va demarrer un cluster (ç a d plusieurs broker)  => là c'est difficile manuellement, il faut utiliser docker:
![image](https://github.com/user-attachments/assets/54abfdf2-9b98-44b3-8c88-7b8983acc40f)
![image](https://github.com/user-attachments/assets/a99478dc-06e5-4616-bbf2-638b42be09df)
![image](https://github.com/user-attachments/assets/168bc628-835d-4076-9fb0-0a204c84790e)
![image](https://github.com/user-attachments/assets/19383de0-f913-4135-b0c1-48afd4e8f399)

=> on va creer 3 instance de zookeper et 3 instance de kafka
lancement avec docker-compose up
![image](https://github.com/user-attachments/assets/3eca2587-b2a1-4cc1-b823-5e62e776e4eb)

travail avec spring:
![image](https://github.com/user-attachments/assets/179ce354-fc92-4458-aabd-36dfd8760d61)
=> un consumer doit appartenir à un groupe (groupe id ) puisque si deux consumers appratient à un seul groupe , un seul consumer va lire le message. (cas queue)
@KafkaLister permet de consomer le message, ; KafkaTempalte permet de produire le message. 
mais si on va utiliser rabbit mq ou jms , ça va pas marcher puisque c'est annotation et classe est dédié pour kafka 
=> on peut utiliser spring cloud stream, qui va fournrir des interfaces qui vont fonctionner qlq soit le type du broker.


ce Schema presente ce qu on va faire:
on va produire des pages event.
On va creer un api qui joue le role d'un produceer.
un consumer
un kafka streams qui va etre l'ecoute de supplier qu'on va le creer qui va tourner chaque 1 seconde pour publier des infos page event:
![image](https://github.com/user-attachments/assets/745457f5-4061-44ab-864a-80539b9b0b9b)

et on va afficher une courbe en temps reel comme ça  (data analytics par kafka streams): 
![image](https://github.com/user-attachments/assets/39cdc153-126f-43fb-9569-8dea2fb5d135)

en rouge page P2 , en vert page P1.

on commence par l'application spring:
-------creer un producer:
![image](https://github.com/user-attachments/assets/2f8591e0-6c5b-454d-bea9-b54f421e51f9)
on va utiliser StreamBridge: si on va inserer un message dans un topic 
generalement ce qu'on va publier sur kafka c'est du json (le PageEvent)
il y a deux solutions soit on utilisant KafkaTemplate(compatible avec Kafka) soit StreamBridge (spring cloud stream, ça va marcher avec n'importe quel broker ...rabitmq...)
----pour creer un consumer maintenant:
![image](https://github.com/user-attachments/assets/2027e19b-2180-42ee-b3bf-5aaadd3007df)
il suffit de creer un bean de type Consumer
et dans application.properties :
on utilise : spring.cloud.stream.bindings.pageEventConsumer-in-0.destination=R1
in c'est input , pageEventConsumer c'est le nom de bean, , R1 c'est le nom de topic
dés qu'on recoit un message sur le topic R1, on va le consomer et la variable output qui va contenir le message.
---pour creer un supplier:
![image](https://github.com/user-attachments/assets/0c50d78e-5907-4a88-a100-111335644327)

le supplier qui va etre lancer chaque 100 ms selon le propertie dans application.properties.
chaque 100ms il va creer un objet PageEvent et le publier dans le topic R2 
le topic ou il va inserer est R2.
c'est comme si un capteur.
dans le console , on voit chaque une 100 ms un message sous format json.

------- pour creer un kafka streams:
![image](https://github.com/user-attachments/assets/f81bdebf-7d20-4905-8e70-fe163b1fcee6)

on va recevoir les inputs depuis le topic R2 
on va utilise Function , pour KStream<String,Double>, le double utilisé pour genrer une valeur utilsié pour statisques.


windowsBy (operation de fenetrage)  ç a d dans les statisque je vais prnedre que les données que j'ai vu avant 30 seconde par exemple. => la moyenne affiché dans l'ecran c est les moyen de pageEvent de 30 derniers seconds en temps reel  => c'est obligatoire ce parameters dans le streaming.
aprés on va renvoyer les données (le stream) vers la topic R4.
le calcul va etre stocker dans une table nommé "count-store"
et aprés on va creer un rest api controller qui lit le count-store et affiche dans le front end.
![image](https://github.com/user-attachments/assets/8cfad980-c368-4a10-b741-4108df8a91bc)
![image](https://github.com/user-attachments/assets/8376d582-d11d-4772-b46c-ddd60133a646)


========================*****TP***==================================================
====================================================================================
creeer un nouveau projet spring , java17 avec ce depedances: 
![image](https://github.com/user-attachments/assets/e44527c5-8e6f-464b-8428-ec2b3f3b1ffa)

let s prepare the docker compose file:
![image](https://github.com/user-attachments/assets/07b2a3d0-b643-4e33-a52a-db015de8a805)

then use command : docker compose up
![image](https://github.com/user-attachments/assets/2982c880-74a3-480f-af38-c3ff43a4f386)
![image](https://github.com/user-attachments/assets/d16c3d41-7c2b-482c-b317-6ffc715b9bff)

ou bien si tu veux ne pas passer par docker:
![image](https://github.com/user-attachments/assets/7f87e294-e027-4682-873f-f6680c9bb022)

on execute la premier commande sur docker:
le consumer:
> docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1
le producer:
> docker exec --interactive --tty bdcc-kafka-broker kafka-console-producer --bootstrap-server broker:9092 --topic T1

dans le producer on va taper "Hello"
![image](https://github.com/user-attachments/assets/03f29914-8ab8-4368-bdd2-5ad6342559a9)
et dans le consumer on remarque que le mot Hello apparu:
![image](https://github.com/user-attachments/assets/732217cc-4757-45e4-9047-2c23bab4e8a7)

le consumer utilise la technique de pulling du coup ça prend plus moins une seconde pour consomer le message

si vous voulez consomer les messages depuis le debut : il faut ajouter dans la commande de consumer : --from-beginning
=> docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1 --from-beginning
![image](https://github.com/user-attachments/assets/555c4634-a9a4-462d-aba6-0383ecebcb75)

on lance notre application sur le port 8080 :
et on fait un appel api via le navigtauer:
![image](https://github.com/user-attachments/assets/8eb14e51-d2a0-4074-a10f-63e47a40db72)

on va lancer un consumer sur un topic T2 pour voir si on a bien reçu le message:
on doit relancer l'api et on remarque qu'on a bien reçu le message dans T2:
![image](https://github.com/user-attachments/assets/f7229e08-9784-4dac-8902-d87d565bf1c1)


maintenant on va creer un consumer coté spring:
![image](https://github.com/user-attachments/assets/6bf26000-280f-4556-9a4a-58f487d86c0a)
on va creer un bean (ç a  d il va etre charger au demarrage de l'application) 
on va creer un objet consumer qui va faire un subscire sur un topic, il faut mettre le topic dans properties qui va etre consomer.
spring.cloud.stream.bindings.pageEventConsumer-in-0.destination=T2
&& spring.cloud.function.definition=pageEventConsumer
![image](https://github.com/user-attachments/assets/40a57cda-66d8-43c2-aa64-fb328af0e31f)


on relancer l'api, et on va remarquer que le message est bien consomé :
![image](https://github.com/user-attachments/assets/06e4657b-e3b8-4321-b492-e836591f6826)

-------------------on va creer un supplier maintenant qui va jouer le role d'un producer qui va pusher un message à chaque minute ou seconde selon les donneés fournit en parameter----
![image](https://github.com/user-attachments/assets/95cae349-613c-4173-be96-9538092221d2)
et dans properties:
spring.cloud.stream.bindings.pageEventSupplier-out-0.destination=T3
&& spring.cloud.function.definition=pageEventConsumer;pageEventSupplier
![image](https://github.com/user-attachments/assets/fe124418-46e5-4883-814a-64bae965f8f7)

on redemarrer l'application et on va creer un consumer qui va lire de T3:
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T3
![image](https://github.com/user-attachments/assets/d0438e25-3aaf-426c-8297-8fe35fa5f57e)
chaque seconde (par defaut) on recoit des nouveaux messages qui sont produits à partir de supplier creer qui presente comme ci un capteur
si on va regler le timer et mettre de 200 ms:
spring.cloud.stream.bindings.pageEventSupplier-out-0.producer.poller.fixed-delay=200

maintenant on va faire du traitement en temps reel , le stream processing avec kafka stream , puisqu'on a des donneés chaque 200ms. qui contient des durations de visites sur lesquel on va se baser



