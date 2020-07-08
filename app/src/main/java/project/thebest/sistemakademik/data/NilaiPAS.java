package project.thebest.sistemakademik.data;

public class NilaiPAS {
    private int no_nilai;
    private int np;
    private String np_predikat;
    private int npt;
    private String npt_predikat;
    private String mapel;

    public NilaiPAS(int no_nilai, int np, String np_predikat, int npt, String npt_predikat, String mapel) {
        this.no_nilai = no_nilai;
        this.np = np;
        this.np_predikat = np_predikat;
        this.npt = npt;
        this.npt_predikat = npt_predikat;
        this.mapel = mapel;
    }

    public int getNo_nilai() {
        return no_nilai;
    }

    public int getNp() {
        return np;
    }

    public String getNp_predikat() {
        return np_predikat;
    }

    public int getNpt() {
        return npt;
    }

    public String getNpt_predikat() {
        return npt_predikat;
    }

    public String getMapel() {
        return mapel;
    }
}
