package project.thebest.sistemakademik.data;

public class Information {
    private int no_info;
    private String judul_info;
    private String deskripsi_info;
    private String media_info;
    private String media_tipe_info;
    private String tanggal_info;

    public Information(int no_info, String judul_info, String deskripsi_info, String media_info, String media_tipe_info, String tanggal_info) {
        this.no_info = no_info;
        this.judul_info = judul_info;
        this.deskripsi_info = deskripsi_info;
        this.media_info = media_info;
        this.media_tipe_info = media_tipe_info;
        this.tanggal_info = tanggal_info;
    }

    public int getNo_info() {
        return no_info;
    }

    public String getJudul_info() {
        return judul_info;
    }

    public String getDeskripsi_info() {
        return deskripsi_info;
    }

    public String getMedia_info() {
        return media_info;
    }

    public String getMedia_tipe_info() {
        return media_tipe_info;
    }

    public String getTanggal_info() {
        return tanggal_info;
    }
}
