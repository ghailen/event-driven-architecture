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



