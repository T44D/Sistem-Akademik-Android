package project.thebest.sistemakademik.data;

public class Presensi {
    private int no_presensi;
    private String nama_hari;
    private String jam;
    private String mapel;
    private String kelas;
    private String keterangan;
    private String tanggal;

    public Presensi(int no_presensi, String nama_hari, String jam, String mapel, String kelas, String keterangan, String tanggal) {
        this.no_presensi = no_presensi;
        this.nama_hari = nama_hari;
        this.jam = jam;
        this.mapel = mapel;
        this.kelas = kelas;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public int getNo_presensi() {
        return no_presensi;
    }

    public String getNama_hari() {
        return nama_hari;
    }

    public String getJam() {
        return jam;
    }

    public String getMapel() {
        return mapel;
    }

    public String getKelas() {
        return kelas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }
}
