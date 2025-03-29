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

On suppose qu'on a un livre avec 100000 pages, et on a 10 personnes. 
on suppose on a 100 partitions ,donc on va diviser les 100000 pages à 100 partitions , du coup il y a aura 1000 pages à distribué , du coup on va disrtribué les 1000 pages au 10 personnes
cahque personne va prendre 100 partition.. 
=> partitionner pour distribuer.
par exemple la personne 1 va prendre la partition 1 jursqu'au 10 , personne 2 va prenddre la partition 11 vers la partition 20 ...
maintenant si personne 1 va tomber en panne (machine) on va perdre ces partitions(les informatiosn).
mais il y a le terme replication.  les partitions de la personne 1 vont avoir une copie dans une autre personne 2 ou 3 .
replica -> creer des copies. => pour resoudre la probleme de tolerance au panne.
pour conculre :
-partitionner , c'est pour distribuer.
-repliquer , c'est pour resoudre le probleme de torelance au panne.

maintneant on suppose que la personne 1 tombe en panne. qui va demander au personne 2 de fournier la copie de la personne 1 qui est malade ou tombé en panne (machine) .
=> c'est zookeeper qui le coordinateur. c'est lui qu va déja demander de repliquer les partietions de P1 dans P2 et P3 , et de P3 dans P5 et P6 ... aprés si quelqu'un tombe on panne c'est lui qui interroge la personne concerné pour fournir la copie. et si un autre P11 qui vient d'etre ajouter , chaque autre personne va donner 2 partitions de leur partition au nouveau personne 11 et on va repartir les partitions vers le nouveau personne et les autres et aussi reassosie les copies.
=> ça c'est la scalabilité: c'est lorsqu'une nouvelle machine qui arrive il faut repartir les partitions sur les nouvelles machines. et le systeme devient plus rapide.
zookeeper qui fait ça , qui permet de gerer les differentes instance de broker.





