# Question 1
Une dizaine de commits sur différentes branches n'ont pas été mergées. Pourquoi ? Quelle a été votre politique des branches ? Comment avez-vous organisé votre travail ?

# Question 2
97 tests qui passent mais tout n'est pas testé. Pourquoi n'avoir pas testé CardExtractor ou CardExtractorRandom ? Discutez de votre méthodologie de tests.

# Question 3
EditDeckController reçoit un MainWindowViewController dans son constructeur. Justifiez ce choix.

# Question 4
Tous les DAO (dans le code Flutter) sont des ensembles de méthodes statiques. Pourquoi ? Est-ce une bonne pratique ?

# Question 5
Vous utilisez le pattern visitor via l'interface "CardVisitor". Pouvez-vous expliquer quel problème vous résolvez avec cette solution ? Pourquoi avez-vous une interface "CardVisitor" et une autre "ExceptionThrowingCardVisitor" ? 

# Question 6
Comment gérez-vous les exceptions qui peuvent survenir lorsque le client effectue une requête (du côté serveur donc) ? Par exemple, dans ulb.infof307.g01.server.handler.DeckRequestHandler#getDeck.

# Question 7
Votre utilisation des DAO va à l'envers de ce qui se fait habituellement: la DB a des références vers les DAOs au lieu de l'inverse. Pourquoi ce choix ? Quels sont les impacts ?

# Question 8
Comment gérez-vous le cas où un utilisateur retire du store son deck publié pendant qu'un autre utilisateur est en train d'y jouer ?

# Question 9
Pourquoi utiliser la classe Optional<T> dans "ServerCommunicator::getDeck" et non des exceptions ?

# Question 10
En l'état actual de votre application, quel serait l'impact d'une requête du client qui renverrait 50MB du point de vue de l'utilisateur ? Que faire pour mitiger ce problème ? 