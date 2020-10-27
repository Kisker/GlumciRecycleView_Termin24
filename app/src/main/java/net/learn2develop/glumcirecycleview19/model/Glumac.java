package net.learn2develop.glumcirecycleview19.model;

public class Glumac {
    // Glumac klasa je drugi korak posle Main Activity. Ona se mora kreirati, kako bismo kreirali posle GlumacAdapter, GlumacProvider i DetailsFragment
        private int id;
        private String ime, prezime, biografija, fotografijaUrl, filmovi;
        private float ocena;
        private String datumRodjenja, datumSmrti;

        public Glumac (){
            this.id = 0;
            this.ime = "";
            this.prezime = "";
            this.biografija = "";
            this.fotografijaUrl = "";
            this.filmovi = "";
            this.ocena = 0;
            this.datumRodjenja = "";
            this.datumSmrti = "";
        }

        public Glumac(int id, String ime, String prezime, String biografija, String fotografijaUrl, String filmovi, float ocena, String datumRodjenja, String datumSmrti) {
            this.id = id;
            this.ime = ime;
            this.prezime = prezime;
            this.biografija = biografija;
            this.fotografijaUrl = fotografijaUrl;
            this.filmovi = filmovi;
            this.ocena = ocena;
            this.datumRodjenja = datumRodjenja;
            this.datumSmrti = datumSmrti;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIme() {
            return ime;
        }

        public void setIme(String ime) {
            this.ime = ime;
        }

        public String getPrezime() {
            return prezime;
        }

        public void setPrezime(String prezime) {
            this.prezime = prezime;
        }

        public String getBiografija() {
            return biografija;
        }

        public void setBiografija(String biografija) {
            this.biografija = biografija;
        }

        public String getFotografijaUrl() {
            return fotografijaUrl;
        }

        public void setFotografijaUrl(String fotografijaUrl) {
            this.fotografijaUrl = fotografijaUrl;
        }

        public String getFilmovi() {
            return filmovi;
        }

        public void setFilmovi(String filmovi) {
            this.filmovi = filmovi;
        }

        public float getOcena() {
            return ocena;
        }

        public void setOcena(float ocena) {
            this.ocena = ocena;
        }

        public String getDatumRodjenja() {
            return datumRodjenja;
        }

        public void setDatumRodjenja(String datumRodjenja) {
            this.datumRodjenja = datumRodjenja;
        }

        public String getDatumSmrti() {
            return datumSmrti;
        }

        public void setDatumSmrti(String datumSmrti) {
            this.datumSmrti = datumSmrti;
        }

        @Override
        public String toString() {
            return "Glumac{" +
                    "id=" + id +
                    ", ime='" + ime + '\'' +
                    ", prezime='" + prezime + '\'' +
                    ", biografija='" + biografija + '\'' +
                    ", fotografijaUrl='" + fotografijaUrl + '\'' +
                    ", filmovi='" + filmovi + '\'' +
                    ", ocena=" + ocena +
                    ", datumRodjenja='" + datumRodjenja + '\'' +
                    ", datumSmrti='" + datumSmrti + '\'' +
                    '}';
        }
}
