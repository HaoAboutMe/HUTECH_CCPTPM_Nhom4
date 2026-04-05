FROM maven:3.9.11-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
# Biên dịch code thành file .jar (bỏ qua chạy thử test để build nhanh)
RUN mvn package -DskipTests

# --- GIAI ĐOẠN 2: RUN ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Chỉ lấy file .jar từ builder (GĐ1)
COPY --from=builder /app/target/*.jar app.jar

ENV DB_URL=jdbc:mysql://host.docker.internal:3306/webbanmaytinh \
    DB_USERNAME=root \
    DB_PASSWORD=123456

EXPOSE 8080
# Lệnh "bật công tắc" để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]