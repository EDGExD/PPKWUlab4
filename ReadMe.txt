1.Podstawowe informacje.

    Aby implementacja konwersji LIVE działała, neleży uruchomić również API z dwóch poprzednich zadań jako oddzielnie działające serwery!

    Po uruchomieniu aplikacji i wpisaniu w przeglądarce localhost:7070 wyświetli się poniższa instrukcja:

    Aby skorzystać z zaimplementowanej funkcjonalności konwersji LIVE jako endpoint należy podać -
            "/LIVE?from=(tutaj format jaki am być pobrany z Api z lab3)&to=(tutaj format na jaki ma być dokonana konwersja)&str=(tutaj dowolny tekst)".

    Aby skorzystać z zaimplementowanej funkcjonalności konwersji ARCHIVE jako endpoint należy podać -
            "/ARCHIVE?to=(tutaj format na jaki ma być dokonana konwersja)&str=(tutaj podaj poprawny otrzymany wcześniej dowolny z 4 formatów tworzonych przez API z lab3);

    API zwróci informacje o podanym stringu w wybranej postać z 3 dostępnych powyżej.
    W przypadku pliku XML, aby uzyskać widok struktury pliku należy wyświetlić kod źródłowy strony.

2. Przykładowe dane.

    Po wpisaniu "http://localhost:7070/LIVE?from=TXT&to=JSON&str=rower" API pobierze dane z API lab3 w postaci TXT i przekonweruje na JSON
        wyświetli się nam informacja: "{"Wielkie litery": 0, "Małe litery": 5, "Cyfry": 0, "Znaki specjalne": 0, "Suma znaków": 5 }".

    Po wpisaniu "http://localhost:7070/ARCHIVE?to=JSON&str=Wielkie%20litery,Ma%C5%82e%20litery,Cyfry,Znaki%20specjalne,Suma%20znak%C3%B3w%200,5,0,0,5" (%20 oznacza znak spacji !!!)
        API rozpozna podany format (w tym przypadku CSV) i zmaiani go na JSON
        wyświetli się nam informacja "{"Wielkie litery": 0, "Małe litery": 5, "Cyfry": 0, "Znaki specjalne": 0, "Suma znaków": 5 }".