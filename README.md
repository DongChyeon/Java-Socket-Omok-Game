## 소켓 기반 1:1 오목 게임
본 프로젝트는 Java의 TCP/IP 소켓 통신과 Swing GUI 컴포넌트를 활용하여 구현한 2인용 오목 게임이다. 사용자는 각각 클라이언트를 실행해 서버에 접속하며, 서버는 두 클라이언트를 매칭하여 하나의 게임 세션을 생성하고, 실시간으로 플레이어 간의 착수 정보를 중계한다. 모든 통신은 텍스트 기반의 간단한 메시지 프로토콜로 이루어지며, 게임 흐름은 서버가 전적으로 관리하는 형태이다.

## 기능
| 착수 | 순서 검사 | 중복 검사 | 승패 검사
|------|------|------|------|
| ![착수](https://github.com/user-attachments/assets/4549969c-774f-48b8-a71f-1db4d822a38d) | ![순서검사](https://github.com/user-attachments/assets/992641ec-1f20-449d-8a39-6c879b8220aa) | ![중복검사](https://github.com/user-attachments/assets/39a7dade-408d-443d-92de-5635828be048) | ![승패검사](https://github.com/user-attachments/assets/147d4ada-c042-43f6-8a5b-01a7d5bf9c93) |


## 시작하는 법
```sh
git clone https://github.com/DongChyeon/Java-Socket-Omok-Game.git
```
git clone을 이용하거나 zip 파일을 다운로드 후 압축 해제하여 실행할 수 있다.

<img width="297" alt="image" src="https://github.com/user-attachments/assets/bde3df35-1808-4c49-a1f4-9d110caeb0f8" />
<br>IntelliJ 환경이라면 프로젝트에 설정되어 있는 Run Configuration을 통해 OmokServer -> OmokClient1 -> OmokClient2 순서대로 실행하면 된다.

<br>OmokServer,  OmokClient (첫번쨰), OmokClient (두번째)를 각각 다른 터미널에서 여는 것이 중요하다.
