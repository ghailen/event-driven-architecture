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


-- hors event driven
stockage distribué on utilise ses bases de donnée , on enregsite ligne par ligne dans chaque machine(example ligne1 dans une machine , ligne2 dans une autre mahcine) , ou colonne par colone dans chaque machine (example colonne user dans une machine , colonne client dans une autre machine)
-MONGO DB : orienté ligne (docuement)
-CASANDRA : orienté colonne
(distribution des tables par colonne ou par ligne) , chaque sgbd a son propre stategrie de distribution.
-NEO4J : orienté graphe dans le cas des reseaux socieaux (resueaux sociaux utilise les graphes , stock les données sous forme des graphoes ) theorie de graphie , il checher les liens, les relations entre les utlisateurs. (le chemin le plus rapide ) aussi les applications de geolocalisation , (de cours chemin ) comme waze,, linkedin neo4j
-Elastic search: sgbd orienté texte , permet de creer des moteurs de recherche,qui permet de chercher par texte pour recurper les documents facilement.
-REDIS (sgbd no sql il est aussi) : ditribue les données sous forme de clé valeur , trés pratique pour creer un cache distribué , cache mémoire. (dans architecture mciroservice utilisé aussi)






