# SMS: Student Management System
> Version *v0.9.0*

## To do
- Hoàn thành menu quản lí ghi danh
- Chuyển DBMS qua SQLite (Hiện tại đang dùng MySQL nên nó hơi rườm rà)

## Hướng dẫn sử dụng
### Yêu cầu
- Java 15 trở lên
- IntelliJ IDEA
- MySQL

### Cài đặt
- Tải hoặc clone bằng IntelliJ IDEA source này về máy
- Tải về:
  - Chọn Code > Download ZIP  
  ![](img/downloadzip.png)
  - Giải nén xong mở IntelliJ IDEA mở thư mục đã giải nén
- Clone:
  - Chọn Code > Bấm nút này để copy link  
  ![](img/clonerepo.png)
  - Mở IntelliJ IDEA chọn Get from VCS  
  ![](img/getfromvcs.png)
  - Copy link vừa rồi vào đây  
  ![](img/gitclone.png)

### Troubleshoot
- MySQL: Phải có hoặc đã setup tên và mật khẩu
- Trong trường hợp không thể sử dụng JDBC để kết nối MySQL thì phải cài driver
  - Tại driver tại [đây](https://cdn.mysql.com//Downloads/Connector-J/mysql-connector-j-8.4.0.zip)
  - Giải nén và để đâu đó trên máy
  - Mở project trong IntelliJ IDEA
  - Bấm `ctrl + alt + shift + S`
  - Vào libraries > Bên phải bấm dấu `+`
  - Chọn file `.jar` trong thư mục hồi nãy giải nén ra
- Cài đặt bằng Clone: trên máy phải có git (nếu chưa có git thì không biết IntelliJ IDEA có tự tải cho mình không mà nếu không thì có thể tự tải ở [đây](https://objects.githubusercontent.com/github-production-release-asset-2e65be/23216272/5f9e16a0-35b4-4c55-a28a-4405fe0e3346?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240513%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240513T202203Z&X-Amz-Expires=300&X-Amz-Signature=65d9072eafe93512d0799e6f45d63e869c20a771de4325054646c2d5888ce8ea&X-Amz-SignedHeaders=host&actor_id=92587073&key_id=0&repo_id=23216272&response-content-disposition=attachment%3B%20filename%3DGit-2.45.0-64-bit.exe&response-content-type=application%2Foctet-stream))