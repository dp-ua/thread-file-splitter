# Консольный потоковый делитель и собиратель файлов.
## Со статистикой
# Console thread file splitter and merger with statistic


### Задача:
> Написать приложение для: 
> * разбиения файла на части
> * соединение файла из нескольких частей
> * разбиение должно производится в несколько потоков
> * отображать статистику работы каждого потока



### Описание приложения:
> После запуска приложения пользователь вводит команду(разбиение или объединение) и параметры команды
> Во время процесса разбиения приложение отображает прогресс (общий и по каждому потоку) 
> в формате: Total progress: 40%, thread 1: 0%, thread 2: 80%, time remaining: 20c. 
> Информация о прогрессе должна выводится каждую секунду. 
> По завершении процесса разбиения приложение ожидает ввода новых команд. 
> Для выхода из программы пользователь должен ввести exit.


### Входящие параметры:
#### split - разбивает файл на части  
> Пример: split -p /home/user/file.avi -s 20M
Аргументы
* -p - путь к файлу
* -s - размер одной части
* -c - или количество частей 

#### merge - соединяет файлы в один
> Пример: merge -p /home/user/file.avi
Аргументы
* -p - путь к файлу

#### exit - завершает работу приложения

### Цели
#### Научится: 
* работать с Maven. Проект должен состоять из нескольких независимых модулей, подключаемых как зависимости
* логировать работу приложения. Используется log4j
* работать с JUnit
* работа с многопоточным приложением
* внедрение зависимостей. Использовать Executor Service таким образом, чтобы он принимал любые подобные задачи не зависимо от их функций. 
