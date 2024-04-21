 /*                  " РОДСТВЕННИКИ "                             */  

predicates
  p(symbol,symbol)
  fem(symbol)
  male(symbol)
  
  mother(symbol,symbol)
  grandmother(symbol,symbol)
  %father(symbol,symbol)
  %grandfather(symbol,symbol)
  sister(symbol,symbol)
  %bro(symbol,symbol)
  aunt(symbol,symbol)
  %uncle(symbol,symbol)
  %niece(symbol,symbol)
  %nephew(symbol,symbol)
  ancestor(symbol,symbol)
  
 
 goal
  sister(beth,X),write(X),nl,fail. 

clauses
 p(mary,bob).         % Мэри - родитель Боба
 p(mary,beth).
 p(tom,beth).
 p(tom,bob).
 p(tom,liz).
 p(bob,ann).
 p(bob,pat).
 p(pat,jim).
 
 fem(mary).              % Мэри - женщина
 fem(beth).
 fem(liz).
 fem(ann).
 fem(pat).
 
 male(tom).              % Том - мужчина
 male(bob).
 male(jim).
 
 mother(X,Y):-           % X - мать Y 
       p(X,Y),
       fem(X).
 
 grandmother(X,Y):-      % X - бабушка Y 
       p(X,Z),
       p(Z,Y),  
       fem(X).
 
             
 sister(X,Y):-           % X - сестра Y
       p(Z,X),
       p(Z,Y),
       fem(X),
       X<>Y .    

 aunt(X,Y):-             % X - тетя Y   
       sister(X,Z),
       p(Z,Y).

 /*niece(X,Y):-          % X - племянница Y (тёти)   
       aunt(Y,X),
       fem(X).
 niece(X,Y):-            % X - племянница Y (дяди)  
       uncle(Y,X),
       fem(X). */
        
 ancestor(X,Z):-         % X - предок Z
       p(X,Z).
 ancestor(X,Z):-
       p(X,Y),
       ancestor(Y,Z).	              
 











    
