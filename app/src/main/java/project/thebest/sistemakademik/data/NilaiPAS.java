package project.thebest.sistemakademik.data;

public class NilaiPAS {
    private int no_nilai;
    private int np;
    private String np_predikat;
    private String np_desc;
    private int npt;
    private String npt_predikat;
    private String npt_desc;
    private String mapel;

    public NilaiPAS(int no_nilai, int np, String np_predikat, String np_desc, int npt, String npt_predikat, String npt_desc, String mapel) {
        this.no_nilai = no_nilai;
        this.np = np;
        this.np_predikat = np_predikat;
        this.np_desc = np_desc;
        this.npt = npt;
        this.npt_predikat = npt_predikat;
        this.npt_desc = npt_desc;
        this.mapel = mapel;
    }

    public int getNo_nilai() {
        return no_nilai;
    }

    public void setNo_nilai(int no_nilai) {
        this.no_nilai = no_nilai;
    }

    public int getNp() {
        return np;
    }

    public void setNp(int np) {
        this.np = np;
    }

    public String getNp_predikat() {
        return np_predikat;
    }

    public void setNp_predikat(String np_predikat) {
        this.np_predikat = np_predikat;
    }

    public String getNp_desc() {
        return np_desc;
    }

    public void setNp_desc(String np_desc) {
        this.np_desc = np_desc;
    }

    public int getNpt() {
        return npt;
    }

    public void setNpt(int npt) {
        this.npt = npt;
    }

    public String getNpt_predikat() {
        return npt_predikat;
    }

    public void setNpt_predikat(String npt_predikat) {
        this.npt_predikat = npt_predikat;
    }

    public String getNpt_desc() {
        return npt_desc;
    }

    public void setNpt_desc(String npt_desc) {
        this.npt_desc = npt_desc;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }
}
