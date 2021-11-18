package com.API.Lab4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@RestController
public class Controller {

    @GetMapping("/LIVE")
    public String live(@RequestParam(defaultValue = "empty") String from, @RequestParam(defaultValue = "empty") String to, @RequestParam String str) {

        String fromFormat = from.toUpperCase(Locale.ROOT);
        String toFormat = to.toUpperCase(Locale.ROOT);

        if (!fromFormat.equals("XML") && !fromFormat.equals("TXT") && !fromFormat.equals("CSV") && !fromFormat.equals("JSON")) {
            return "Podano błędny format pliku wejsciowego (parametr \"from\") - " + from;
        }

        if (!toFormat.equals("XML") && !toFormat.equals("TXT") && !toFormat.equals("CSV") && !toFormat.equals("JSON")) {
            return "Podano błędny format pliku wyjściowego (parametr \"to\")";
        }

        String url = "http://localhost:6060/" + fromFormat + "?str=" + str;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        if(result==null) return "Nie udało się połączyć z API z zadania 3";

        if (fromFormat.equals("TXT") && toFormat.equals("TXT")) return result;


        String result2=null;

        switch (fromFormat) {
            case "XML":
                result2 = convertXMLtoString(result);
                break;
            case "CSV":
                result2 = convertCSVtoString(result);
                break;
            case "JSON":
                result2 = convertJSONtoString(result);
                break;
            default:
                result2 = convertTXTtoString(result);
                break;
        }

        switch (toFormat) {
            case "XML":
                return giveXML(result2);
            case "CSV":
                return giveCSV(result2);
            case "JSON":
                return giveJSON(result2);
            default:
                return result2;
        }

    }

    @GetMapping("/ARCHIVE")
    public String archive(@RequestParam String to, @RequestParam String str) {

        String toFormat = to.toUpperCase(Locale.ROOT);
        if (!toFormat.equals("XML") && !toFormat.equals("TXT") && !toFormat.equals("CSV") && !toFormat.equals("JSON")) {
            return "Podano błędny format pliku wyjściowego (parametr \"from\")";
        }


        String result=null;
        System.out.println(str);

        if(str.charAt(0)=='{')
        {
            result=convertJSONtoString(str);
        }
        else if(str.charAt(0)=='<')
        {
            result=convertXMLtoString(str);
        }
        else if(str.charAt(14)==',')
        {
            result=convertCSVtoString(str);
        }
        else
        {
            result = convertTXTtoString(str);
        }

        switch (toFormat){
            case "CSV":
                return giveCSV(result);
            case "XML":
                return giveXML(result);
            case "JSON":
                return giveJSON(result);
            default:
                return giveTXT(result);
        }

    }


    @GetMapping("/")
    public String help() {
        return "Aby skorzystać z zaimplementowanej funkcjonalności konwersji LIVE jako endpoint należy podać - " +
                "\"/LIVE?from=(tutaj format jaki am być pobrany z Api z lab3)" +
                "&to=(tutaj format na jaki ma być dokonana konwersja)" +
                "&str=(tutaj dowolny tekst)\"<br><br>" +
                "Aby skorzystać z zaimplementowanej funkcjonalności konwersji ARCHIVE jako endpoint należy podać - " +
                "\"/ARCHIVE?to=(tutaj format na jaki ma być dokonana konwersja)" +
                "&str=(tutaj podaj poprawny otrzymany wcześniej dowolny z 4 formatów tworzonych przez API z lab3)\"<br>";
    }


    public String convertJSONtoString(String str)
    {
        String substr = str;

        if(str.charAt(0)!='{'){ return null;}

        StringBuilder sb = new StringBuilder();
        sb.append("Podany string posiada:<br>");
        substr = substr.substring(substr.indexOf(':') + 2);

        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków zapisanych wielką literą<br>");

        //sb.append("Małe litery:");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków zapisanych małą literą<br>");

        //sb.append("Cyfry");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków będących cyfrą<br>");

        //sb.append("Znaki specjalne: ");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków specjalnych<br>");

        //sb.append("Suma znaków: ");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf('}')));
        sb.append(" - suma znaków.");


        return sb.toString();
    }

    public String convertCSVtoString(String str)
    {
        String substr = str;

        substr = substr.substring(61);

        StringBuilder sb = new StringBuilder();
        sb.append("Podany string posiada:<br>");
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków zapisanych wielką literą<br>");

        substr = substr.substring(substr.indexOf(',') + 1);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków zapisanych małą literą<br>");

        //sb.append("Cyfry");
        substr = substr.substring(substr.indexOf(',') + 1);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków będących cyfrą<br>");

        //sb.append("Znaki specjalne: ");
        substr = substr.substring(substr.indexOf(',') + 1);
        sb.append(substr.substring(0, substr.indexOf(',')));
        sb.append(" - znaków specjalnych<br>");

        //sb.append("Suma znaków: ");
        substr = substr.substring(substr.indexOf(',') + 1);
        sb.append(substr.substring(0));
        sb.append(" - suma znaków.");


        return sb.toString();
    }

    public String convertTXTtoString(String str)
    {
        String substr = str;

        StringBuilder sb = new StringBuilder();
        sb.append("Podany string posiada:<br>");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf('\n')));
        sb.append(" - znaków zapisanych wielką literą<br>");

        //sb.append("Małe litery:");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf('\n')));
        sb.append(" - znaków zapisanych małą literą<br>");

        //sb.append("Cyfry");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf('\n')));
        sb.append(" - znaków będących cyfrą<br>");

        //sb.append("Znaki specjalne: ");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0, substr.indexOf('\n')));
        sb.append(" - znaków specjalnych<br>");

        //sb.append("Suma znaków: ");
        substr = substr.substring(substr.indexOf(':') + 2);
        sb.append(substr.substring(0));
        sb.append(" - suma znaków.");


        return sb.toString();
    }

    public String convertXMLtoString(String str)
    {
        String substr = str;

        substr = substr.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".length() + "<StringInfo>\n".length() + "<WielkieLitery>".length() );

        StringBuilder sb = new StringBuilder();
        sb.append("Podany string posiada:<br>");
        sb.append(substr.substring(0, substr.indexOf('<')));
        sb.append(" - znaków zapisanych wielką literą<br>");

        substr = substr.substring(substr.indexOf('>') + 1);
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf('<')));
        sb.append(" - znaków zapisanych małą literą<br>");

        //sb.append("Cyfry");
        substr = substr.substring(substr.indexOf('>') + 1);
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf('<')));
        sb.append(" - znaków będących cyfrą<br>");

        //sb.append("Znaki specjalne: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf('<')));
        sb.append(" - znaków specjalnych<br>");

        //sb.append("Suma znaków: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0));
        sb.append(" - suma znaków.");


        return sb.toString();
    }

    public String giveCSV(String str) {
        String substr = str;

        StringBuilder sb = new StringBuilder();
        sb.append("Wielkie litery");
        sb.append(',');
        sb.append("Małe litery");
        sb.append(',');
        sb.append("Cyfry");
        sb.append(',');
        sb.append("Znaki specjalne");
        sb.append(',');
        sb.append("Suma znaków");
        sb.append('\n');

        for (int i = 0; i < 4; i++) {
            substr = substr.substring(substr.indexOf('>') + 1);
            sb.append(substr.substring(0, substr.indexOf(' ')));
            sb.append(',');
        }

        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append('\n');


        return sb.toString();
    }

    public String giveJSON(String str) {
        String substr = str;

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"Wielkie litery\": ");

        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append(",\n");

        sb.append("\"Małe litery\": ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append(",\n");

        sb.append("\"Cyfry\": ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append(",\n");

        sb.append("\"Znaki specjalne\": ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append(",\n");

        sb.append("\"Suma znaków\": ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append('\n');
        sb.append('}');

        return sb.toString();
    }

    public String giveTXT(String str) {
        String substr = str;

        StringBuilder sb = new StringBuilder();

        sb.append("Wielkie litery: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("\n");

        sb.append("Male litery: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("\n");

        sb.append("Cyfry: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("\n");

        sb.append("Znaki specjalne: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("\n");

        sb.append("Suma znaków: ");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("\n");

        return sb.toString();
    }

    public String giveXML(String str) {
        String substr = str;

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<StringInfo>\n");

        sb.append("<WielkieLitery>");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("</WielkieLitery>\n");

        sb.append("<MaleLitery>");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("</MaleLitery>\n");

        sb.append("<Cyfry>");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("</Cyfry>\n");

        sb.append("<ZnakiSpecjalne>");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("</ZnakiSpecjalne>\n");

        sb.append("<SumaZnaków>");
        substr = substr.substring(substr.indexOf('>') + 1);
        sb.append(substr.substring(0, substr.indexOf(' ')));
        sb.append("</SumaZnaków>\n");
        sb.append("</StringInfo>\n");

        return sb.toString();
    }
}