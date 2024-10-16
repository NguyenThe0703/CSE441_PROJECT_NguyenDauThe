package com.example.cse441_project.Model;

public class Employee {
    private String maNV;
    private String tenDN;
    private String phanQuyen;
    private String matKhau;
    private String tenNV;
    private String gioiTinh;
    private String ngaySinh;
    private String cmnd;

    // Constructor rỗng cần thiết cho Firebase
    public Employee() {}

    // Constructor đầy đủ
    public Employee(String maNV, String tenDN, String phanQuyen, String matKhau, String tenNV, String gioiTinh, String ngaySinh, String cmnd) {
        this.maNV = maNV;
        this.tenDN = tenDN;
        this.phanQuyen = phanQuyen;
        this.matKhau = matKhau;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.cmnd = cmnd;
    }

    // Getter và Setter cho từng thuộc tính
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenDN() { return tenDN; }
    public void setTenDN(String tenDN) { this.tenDN = tenDN; }

    public String getPhanQuyen() { return phanQuyen; }
    public void setPhanQuyen(String phanQuyen) { this.phanQuyen = phanQuyen; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getCmnd() { return cmnd; }
    public void setCmnd(String cmnd) { this.cmnd = cmnd; }
}
