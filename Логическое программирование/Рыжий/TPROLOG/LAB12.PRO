/* Процессор вывода для маленькой экспертной системы */

predicates

  animal_is(symbol)
  it_is(symbol)
  positive(symbol, symbol)
  negative(symbol, symbol)
  ask(symbol,symbol,symbol)
  remember(symbol,symbol,symbol)
  clear_facts
  run

database
  dpositive(symbol,symbol)
  dnegative(symbol,symbol)

goal
  clear_facts,
  run.

clauses

   animal_is(octopus) :-             % осьминог
      it_is(wild),
      not(it_is(mammal)),            % НЕ млекопитающее
      it_is(carnivore),              % плотоядное
      positive(does, sweem),         % плавает
      positive(has, tentacles).      % имеет щупальца

   animal_is(sardine) :-                 % сардина
      it_is(wild),
      it_is(fish),                       % рыба
      positive(does,small),              % маленькая
      positive(does,use_in_sandwiches).  % используется в бутербродах

   animal_is(cheetah) :-             % гепард
      it_is(wild),
      it_is(mammal),                 % млекопитающее
      it_is(carnivore),              % плотоядное
      positive(has, tawny_color),    % желтовато-коричневый цвет
      positive(has, dark_spots).     % темные пятна

   animal_is(tiger) :-               % тигр
      it_is(wild),
      it_is(mammal),                 % млекопитающее
      it_is(carnivore),              % плотоядное
      positive(has, tawny_color),    % желтовато-коричневый окрас
      positive(has, black_stripes).  % черные полосы

   animal_is(giraffe) :-             % жираф
      it_is(wild),
      it_is(ungulate),               % копытное
      positive(has, long_neck),      % длинная шея
      positive(has, long_legs),      % длинные ноги
      positive(has, dark_spots).     % темные пятна

   animal_is(zebra) :-               % зебра
      it_is(wild),
      it_is(ungulate),               % копытное
      positive(has,black_stripes).   % черные полосы

   animal_is(ostrich) :-             % страус
      it_is(wild),
      it_is(bird),                   % птица
      negative(does, fly),           % летает
      positive(has, long_neck),      % длинная шея
      positive(has, long_legs),      % длинные ноги
      positive(has, black_and_white_color). % черно-белый окрас

   animal_is(penguin) :-             % пингвин
      it_is(wild),
      it_is(bird),                   % птица
      negative(does, fly),           % не летает
      positive(does, swim),          % плавает
      positive(has, black_and_white_color). % черно-белый окрас

   animal_is(albatross) :-           % альбатрос
      it_is(wild),
      it_is(bird),                   % птица
      positive(does, fly_well).      % хорошо летает

   animal_is(cat) :-                 % кот
      it_is(domestic),               % домашний
      it_is(mammal),                 % млекопитающее
      it_is(carnivore),              % ест мясо
      positive(does, meow).          % мяукает

animal_is(parrot) :-
      it_is(domestic),                    %Домашнее
      it_is(bird),                        %Птица
      positice(does, speak),              %Разговаривает
      positive(has, colorful_feathers).   %Имеет разноцветные перья


   it_is(mammal) :- positive(has, hair).       % имеет шерсть
   it_is(mammal) :- positive(does, give_milk). % дает молоко
   it_is(bird)   :- positive(has, wings).   % имеет крылья
   it_is(bird)   :- positive(does, fly),
                   positive(does,lay_eggs).   % летает, несет яйца
   it_is(fish)   :- positive(does, sweem),     % плавает
                       positive(has, finth).  % имеет плавники

   it_is(domestic) :- 
      negative(does, hunt),               %Не охотится
      negative(does, angry),              %Не агресивный
      negative(does, live_in_wild).       %Не выживет в дикой природе

   it_is(wild) :-
      not(it_is(demestic)).

   it_is(carnivore) :-
     positive(does, eat_meat).       % ест мясо

   it_is(carnivore) :-
     positive(has, pointed_teeth),   % имеет острые зубы
     positive(has, claws),           % имеет когти
     positive(has, forward_eyes).    % имеет глаза, направленные
                                     % вперед

   it_is(ungulate) :-
     it_is(mammal), 
     positive(has, hooves).    % имеет копыта

   it_is(ungulate) :-
     it_is(mammal), 
     positive(does, chew_cud). % жвачное

   positive(X, Y) :-
     dpositive(X, Y), !.
   positive(X, Y) :-
     not(dnegative(X, Y)),
     ask(X, Y, yes),!.
   positive(X, Y) :-
     not(dnegative(X, Y)),
     remember(X,Y,no),!,fail.

   negative(X, Y) :-
      dnegative(X, Y), !.
   negative(X, Y) :-
      not(dpositive(X, Y)),
      ask(X, Y, no),!.
   negative(X, Y) :-   
      not(dpositive(X, Y)),
      remember(X,Y,yes),!,fail.
     
  /* Задание вопросов и анализ ответов */

   ask(X, Y, yes) :-  !, write(X, " it ", Y, '\n'),
      readln(Reply),
      upper_lower(Reply,Reply1),
      frontchar(Reply1,  'y', _),
      remember(X, Y, yes),!.

   ask(X, Y, no) :-  !, write(X, " it ", Y, '\n'),
      readln(Reply),
      upper_lower(Reply,Reply1),
      frontchar(Reply1, 'n', _),
      remember(X, Y, no),!.

   remember(X, Y, yes) :- assertz(dpositive(X,Y)),!.
   remember(X, Y, no)  :- assertz(dnegative(X,Y)),!.

   /* Уничтожение всех старых фактов */
   /* dbasedom - стандартный домен для обозначения базы данных */
   /* database, не имеющей индивидуального имени                 */

   clear_facts:-
      retractall(_, dbasedom).

    run :-
      animal_is(X), !,
      write("\nYour animal should be ", X),
      nl, nl, save("animal.dat"),clear_facts,
      write("Press space to escape" ),readchar(_).

   run :-
      write("\nYour animal cannot "),
      write(" be identified.\n\n"),save("aaa.dat"),
      clear_facts, write("Press space to escape" ),
      clearwindow,
      readchar(_).
