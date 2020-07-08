package project.thebest.sistemakademik.data;

public class NilaiPTS {
    private int no_nilai;
    private int np1;
    private int np2;
    private int np3;
    private int np4;
    private int np5;
    private int nk1;
    private int nk2;
    private int nk3;
    private int nk4;
    private int nk5;
    private int pts;
    private String mapel;

    public NilaiPTS(int no_nilai, int np1, int np2, int np3, int np4, int np5, int nk1, int nk2, int nk3, int nk4, int nk5, int pts, String mapel) {
        this.no_nilai = no_nilai;
        this.np1 = np1;
        this.np2 = np2;
        this.np3 = np3;
        this.np4 = np4;
        this.np5 = np5;
        this.nk1 = nk1;
        this.nk2 = nk2;
        this.nk3 = nk3;
        this.nk4 = nk4;
        this.nk5 = nk5;
        this.pts = pts;
        this.mapel = mapel;
    }

    public int getNo_nilai() {
        return no_nilai;
    }

    public int getNp1() {
        return np1;
    }

    public int getNp2() {
        return np2;
    }

    public int getNp3() {
        return np3;
    }

    public int getNp4() {
        return np4;
    }

    public int getNp5() {
        return np5;
    }

    public int getNk1() {
        return nk1;
    }

    public int getNk2() {
        return nk2;
    }

    public int getNk3() {
        return nk3;
    }

    public int getNk4() {
        return nk4;
    }

    public int getNk5() {
        return nk5;
    }

    public int getPts() {
        return pts;
    }

    public String getMapel() {
        return mapel;
    }
}
