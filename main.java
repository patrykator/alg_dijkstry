import java.util.*;

class Graf {
    private Map<Character, Map<Character, Integer>> wierzcholki;

    public Graf() {
        this.wierzcholki = new HashMap<>();
    }

    public void dodajWierzcholek(char wierzcholek) {
        if (!wierzcholki.containsKey(wierzcholek)) {
            wierzcholki.put(wierzcholek, new HashMap<>());
        }
    }

    public void dodajKrawedz(char wierzcholek1, char wierzcholek2, int waga) {
        dodajWierzcholek(wierzcholek1);
        dodajWierzcholek(wierzcholek2);
        wierzcholki.get(wierzcholek1).put(wierzcholek2, waga);
        wierzcholki.get(wierzcholek2).put(wierzcholek1, waga);
    }

    public Map<Character, Integer> getSasiadow(char wierzcholek) {
        return wierzcholki.getOrDefault(wierzcholek, new HashMap<>());
    }

    public Map<Character, Object> dijkstra(char start, char koniec) {
        Map<Character, Integer> odleglosci = new HashMap<>();
        Map<Character, Character> poprzednicy = new HashMap<>();
        PriorityQueue<Character> kolejka = new PriorityQueue<>(Comparator.comparingInt(odleglosci::get));

        for (char wierzcholek : wierzcholki.keySet()) {
            odleglosci.put(wierzcholek, Integer.MAX_VALUE);
        }

        odleglosci.put(start, 0);
        kolejka.add(start);

        while (!kolejka.isEmpty()) {
            char aktualny = kolejka.poll();

            for (Map.Entry<Character, Integer> sasiad : wierzcholki.get(aktualny).entrySet()) {
                char sasiadWierzcholka = sasiad.getKey();
                int wagaKrawedzi = sasiad.getValue();
                int nowaOdleglosc = odleglosci.get(aktualny) + wagaKrawedzi;

                if (nowaOdleglosc < odleglosci.get(sasiadWierzcholka)) {
                    odleglosci.put(sasiadWierzcholka, nowaOdleglosc);
                    poprzednicy.put(sasiadWierzcholka, aktualny);
                    kolejka.add(sasiadWierzcholka);
                }
            }
        }

        LinkedList<Character> sciezka = new LinkedList<>();
        char aktualny = koniec;
        while (poprzednicy.containsKey(aktualny)) {
            sciezka.addFirst(aktualny);
            aktualny = poprzednicy.get(aktualny);
        }
        sciezka.addFirst(start);

        Map<Character, Object> wynik = new HashMap<>();
        wynik.put(koniec, odleglosci.get(koniec));
        wynik.put('N', sciezka.size() - 1);
        wynik.put('S', sciezka);
        return wynik;
    }
}

public class Main {
    public static void main(String[] args) {
        Graf graf = new Graf();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj liczbę krawędzi:");
        int liczbaKrawedzi = scanner.nextInt();

        System.out.println("Podaj krawędzie w formacie: wierzchołek1 wierzchołek2 waga");
        for (int i = 0; i < liczbaKrawedzi; i++) {
            char wierzcholek1 = scanner.next().charAt(0);
            char wierzcholek2 = scanner.next().charAt(0);
            int waga = scanner.nextInt();
            graf.dodajKrawedz(wierzcholek1, wierzcholek2, waga);
        }

        System.out.println("Podaj wierzchołki między którymi chcesz obliczyć najkrótszą ścieżkę:");
        char start = scanner.next().charAt(0);
        char koniec = scanner.next().charAt(0);

        Map<Character, Object> wynik = graf.dijkstra(start, koniec);
        System.out.println("Najkrótsza ścieżka między " + start + " a " + koniec + " wynosi: " + wynik.get(koniec));
        System.out.println("Długość najkrótszej ścieżki: " + wynik.get('N'));
        System.out.println("Ścieżka: " + wynik.get('S'));

        scanner.close();
    }
}
