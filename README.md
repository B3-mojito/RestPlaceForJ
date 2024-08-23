### 🔍 주요 기능


<p align="center">
    <img src="https://github.com/user-attachments/assets/a68d6ef7-8ef8-449c-a532-9a410917ea48">
</p>

- **여행지 추천**
    - 지역과 테마를 골라 여행지를 추천받을 수 있습니다.
        
        ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/1be6f187-f051-47f2-bc68-23daf198fb1a/image.png)
        
    - 추천 받은 여행지에 대해 유저들이 작성한 게시글을 조회할 수 있습니다.
        
        
        ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/efc69f16-eaeb-41a2-aadb-a2733aeb5dcf/image.png)
        
        ![스크린샷 2024-08-16 오전 11.04.24.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/48feb5aa-a168-4188-8655-493f77e3439c/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-08-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.04.24.png)
        
- **여행 계획**
    - 칸반보드 형식으로 여행 일정을 관리할 수 있습니다.
    - 유저를 초대하여 함께 여행 일정을 계획할 수 있습니다.
    
    ![스크린샷 2024-08-16 오전 11.42.24.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/21d21d8a-15b1-4930-9f36-7f38b0ec778b/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-08-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.42.24.png)
    
    ![스크린샷 2024-08-16 오전 11.42.36.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/f17cf7d0-83c9-4ea6-bf2c-ac546cb55054/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-08-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.42.36.png)
    

---

### 🏗 서비스 아키텍처

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/a423fd5a-9752-4aad-9096-0e2992137723/image.png)

### 🍀 주요 기술

| JAVA 17 | LTS 버전으로 안정성이 높을 것이라 판단하였고, 사용한 Spring Boot 3.x 버전과의 호환을 위해 사용하였습니다. |
| --- | --- |
| GitHub Actions | CI/CD를 구현하기 위해 사용하였습니다. 대규모 파이프라인을 구현할 필요가 없다고 판단하여 워크플로우를 간소화할 수 있는 GitHub Actions를 채택하였습니다. |
| Docker | 컨테이너 환경에서 애플리케이션 배포를 자동화할 수 있게 하기 위해 사용하였습니다. |
| AWS Route 53 | 도메인 구매, 프론트엔드 주소 설정, ALB로 요청을 라우팅하여 전달하기 위해 사용하였습니다. |
| AWS Elastic Load Balancer | 특정 서버에 부하가 몰리지 않도록 클라이언트 요청을 균등 분배하여 서버 과부하를 막기 위해 사용하였습니다. |
| AWS EC2 | 백엔드 서버 배포를 위해 사용하였습니다. 직접 서버를 구축하는 대신 편리하게 가상 서버를 런칭하고 종료하기 위해 AWS EC2를 선택하였습니다. 또한 부하를 관리하기 위해 2개의 EC2 인스턴스를 생성하였습니다. |
| AWS RDS | 도커에 MySQL를 띄우는 것 보다 간편하게 관계형 데이터베이스 서비스를 이용할 수 있어 채택하였습니다. |
| AWS ElastiCache
(Redis) | 도커에 Redis를 띄우는 것 보다 간편하게 인메모리 캐싱 서비스를 이용 할 수 있어 채택하였습니다. 인메모리 캐싱은 RefreshToken과 이메일 인증 코드, 캐시 데이터, 동시성 제어등을 위해 사용하였습니다. |
| AWS S3 | 유저 프로필 이미지와 글 이미지를 저장하기 위해 사용하였습니다.  |
| React | 프론트엔드를 구현하기 위해 사용하였습니다. 러닝커브가 높다는 단점이 있지만 
사용하는 개발자가 많아서 쉽게 에러 해결 방법을 찾을수 있고, 컴포넌트를 사용하기 때문에 유지보수성와 개발 생산성이 높다는 베네핏을 위해 채택했습니다. |
| Vercel  | EC2 등으로 서버를 띄워 배포하는 방법도 있지만 ETA를 맞추기 위해 간편하게 배포할 수 있는 Vercel을 채택했습니다. 또한 Vercel은 파이프라인을 따로 구축할 필요 없이 자동화된 CI/CD가 가능하기 때문에 Vercel을 채택했습니다. |

### 🗣️ 기술적 의사결정

- **Redisson**
    1. **기술 도입 필요성**
        - RefreshToken, 이메일 인증 코드 등 TTL이 있는 데이터를 저장해야 하기 때문에 In-memory DB를 도입해야 했습니다.
        - 동시성 제어를 위해 분산락을 사용하기로 했기 때문에 동시 접근을 관리해줄 시스템이 필요했습니다.
        - 포스트 등 자주 조회되는 데이터를 캐싱 처리하기로 했기 때문에 In-memory DB를 도입해야 했습니다
    2. **Memcached vs Redisson**
        - Memcached는 작은 데이터를 임시 저장하고, 멀티 쓰레드를 활용하여 다중 처리가 빠르다는 장점이 있습니다. 하지만 오로지 메모리에만 데이터를 저장하기 때문에 메모리가 부족할 경우 일부 데이터를 삭제하는 문제가 있습니다. 또한 데이터 타입을 String만 지원하는 단점이 있습니다.
        - 반면 Redisson는 String 외 여러가지의 데이터 타입을 제공합니다. 또한 스냅샷을 통해 데이터를 디스크에 담기 때문에 메모리의 여유 공간을 만들 수 있어 Memcached의 데이터 삭제 문제가 발생하지 않습니다.
        
        ⇒ 이후의 확장성과 데이터 보관을 고려해보았을 때 Memcached보다는 Redisson를 선택하는 것이 좋다고 생각하여 Redisson를 선택하였습니다.
        
- **AWS ElastiCache**
    1. **기술 도입 필요성**
        - Redis를 도입했기 때문에 서버에 Redis를 올려야 했습니다.
    2. **Docker vs AWS ElastiCache**
        - 도커에 Redis를 올려서 인프라를 구축할 수 있지만, 러닝커브가 높아 촉박한 ETA를 맞추기에 적절하지 않다고 판단했습니다.
    
    ⇒ 이를 대체할 수 있는 방법으로 ElastiCache를 알게되었는데, 전자의 방법에 비해 러닝 커브가 낮고 쉽게 도입할 수 있다고 생각하여 ElastiCache를 선택하게 되었습니다.
    
- **Github Action**
    1. **기술 도입 필요성**
        - 자동화된 파이프라인을 통해 CI/CD를 진행해야 했습니다.
    2. **Github Action vs Jenkins**
        - Jenkins는 자료가 많지만 CI/CD 구성이 어렵고 별도의 서버 설치가 필요합니다.
        - Github Action은 자료가 적지만 CI/CD 구성이 쉽고 별도의 서버 설치가 필요하지 않습니다.
    
    ⇒ 저희 프로젝트의 규모를 고려해보았을 때, Jenkins는 설정시 많은 리소스가 발생하므로 부담이 적은 Github Action을 선택하는 것이 좋다고 판단하여 Github Action을 선택하였습니다.
    
- **동시성 제어**
    1. **기술 도입 필요성**
        - 좋아요 기능 테스트 중 동시성 문제가 발생하여 동시성 제어를 도입해야 했습니다. (관련 트러블 슈팅은 `Query did not return a unique result: 2 results were returned 문제` ****에서 확인하실 수 있습니다.)
    2. **낙관적 락 vs 비관적 락 vs 분산락**
        - **낙관적 락**은 데이터를 수정할 때 다른 트랜잭션이 데이터를 수정했는지를 확인하여, 변경 사항이 있을 경우 예외를 발생시키고 롤백하는 방식입니다.  주로 트랜잭션 충돌이 드문 경우, 데이터의 읽기 작업이 많은 환경, 높은 동시성 요구가 있지만 충돌 가능성이 낮은 경우에 사용합니다.
        - **비관적 락**은 특정 데이터에 대한 접근을 제한하여, 하나의 트랜잭션이 완료될 때까지 다른 트랜잭션이 해당 데이터에 접근하지 못하도록 하는 방식입니다. 주로 트랜잭션 충돌이 자주 발생할 것으로 예상되는 경우, 중요한 데이터의 무결성이 강력하게 요구되는 경우, 동시 접근이 많지 않은 경우에 사용합니다.
        - **분산 락**은 여러 서버가 동일한 자원에 동시에 접근하는 것을 제어하기 위해 설계된 메커니즘입니다. Redis, Zookeeper와 같은 외부 시스템을 이용하여 락을 관리합니다. 주로 분산된 시스템에서 동일한 자원을 보호해야 할 때, 데이터베이스의 인스턴스가 분산되어 있는 경우에 사용합니다.
        
        ⇒ 저희는 단일 서버가 아닌 분산 시스템으로 이루어져 있기 때문에 분산 락을 선택하였습니다. 
        
- **캐싱 처리**
    1. **기술 도입 필요성**
        - 데이터 검색 지연 시간을 줄이고 애플리케이션의 전반적인 성능 향상을 위해 캐싱 처리를 도입하기로 하였습니다.

### 🛠 트러블 슈팅

- **Mixed Contents**
    - **1. 문제 상황**
        - 프론트 배포 배포 후 백엔드 서버와 연결을 시도하니, 브라우저에서  `Mixed Contents` 에러가 발생하여 백엔드 서버와 연결이 불가했습니다.
        
        ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/ea89378e-bf12-4d3b-9a50-07577bc34696/image.png)
        
    - **2. 원인**
        - `Mixed Contents` 란 웹 페이지에서 보안 연결(HTTPS)을 통해 제공되는 페이지에 보안되지 않은 연결(HTTP)를 통해 로드되는 컨텐츠가 있을 경우 발생하는 오류입니다.
        - 저희 프로젝트의 프론트 서버는 HTTPS로 배포가 되었지만, 백엔드 서버는 HTTP로 배포되었기 때문에  `Mixed Contents` 가 발생하였습니다.
    - **3. 해결 방법 탐색 및 해결**
        - **해결 방법 탐색**
            - 유저 테스트 ETA가 다가오고 있었기 때문에 문제를 빠르게 해결해야 했습니다. 프론트 서버를 HTTP로 다운 그레이드 하는 방법을 채택하면 빠르게 문제를 해결할 수 있습니다.
            - 하지만 HTTP은 모든 사람이 모든 요청과 응답을 읽을 수 있어 악의적인 공격자에 의해 정보가 갈취 당할 수 있어 보안상 부적절하다고 판단하여, 시간이 빠듯하더라도 백엔드 서버를 HTTPS로 업그레이드 하기로 결정하였습니다.
        - **해결 방법**
            - `SSL Termination` 방식을 채택하여 문제를 해결하였습니다.
                - `SSL Termination`은 로드 밸런서에서 HTTP로 온 요청은 HTTPS 요청으로 리다이렉트 시키고, 
                HTTPS로 온 요청은 SSL/TLS 암호화를 해제(terminate)하여 EC2 인스턴스와는 암호화되지 않은 평문 HTTP로 통신하는 방식입니다.
                - `SSL Termination` 을 사용하면 인증서 복호화를 EC2 서버가 아닌 로드 밸런서에서 처리할 수 있으므로 EC2 서버의 부하를 줄일 수 있다는 장점이 있습니다.
                - 또한 EC2 서버를 여러 대 가질 때 모든 EC2 서버에서 인증서를 설치, 관리할 필요 없이 로드 밸런서에서 관리할 수 있어 편리하다는 장점이 있습니다.
            - `SSL Termination` 방식을 이용하기 위해 먼저 AWS Certificate Manager에서 인증서를 발급하였습니다.
                
                ![스크린샷 2024-08-17 오후 1.34.17.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/b65c4798-2e38-427c-ae6f-bdfc7bae7256/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-08-17_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_1.34.17.png)
                
            
            - AWS Route53를 이용하여 도메인 이름을 호스팅 영역에 등록하고, DNS 설정을 통해 서버와 로드 밸런서와 관련한 레코드를 등록했습니다. 발급받은 인증서를 로드 밸런서에 설정하여 SSL Termination을 구성했습니다.
                - 서버와 로드 밸런서와 관련한 레코드를 등록
                    
                    ![스크린샷 2024-08-17 오후 1.37.03.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/716e17df-69ed-400c-9dfa-20dbf693a5a5/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-08-17_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_1.37.03.png)
                    
                - 로드밸런서에서 HTTP로 온 요청은 HTTPS로 리디렉션 되도록 설정하고, HTTPS 요청은 EC2 인스턴스로 전달
                    
                    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/6e81b315-8d9b-4731-84ed-6780a45d9044/image.png)
                    
- **Query did not return a unique result: 2 results were returned 문제**
    - **1. 문제 상황**
        - 댓글, 게시물 좋아요 테스트 중, 백엔드 서버에서 좋아요 로직이 처리되기 전 추가적으로 좋아요 요청이 들어오면 `Query did not return a unique result: 2 results were returned` ****에러가 발생하는 문제가 발견되었습니다.
    - **2. 원인**
        - `Query did not return a unique result: 2 results were returned` 은 데이터베이스 쿼리가 실행될 때, 단일 결과가 반환될 것으로 예상했지만 실제로는 두 개 이상의 결과가 반환되었을 때 발생하는 오류입니다. 이 오류는 다음과 같은 경우에 발생할 수 있습니다.
            - **중복된 데이터**: 쿼리가 예상하지 못한 중복 데이터를 반환하는 경우. 예를 들어, 특정 조건을 만족하는 두 개 이상의 레코드가 데이터베이스에 존재할 때 이 오류가 발생할 수 있습니다.
            - **잘못된 쿼리 작성**: 쿼리를 실행할 때, 여러 결과를 반환할 수 있는 조건을 잘못 설정한 경우. 단일 결과를 반환해야 하는데, 여러 결과가 반환되는 경우입니다.
        
        ```jsx
        *if* (postLikeRepository.existsByPostAndUser(post, user)){
           post.removeLikeFromPost();  
           postRepository.save(post); 
           PostLike findPostLike = postLikeRepository.findByPostLikeOrThrow(post, user); 
           postLikeRepository.delete(findPostLike);
        	*return* Optional.*empty*();
        	}
           post.addLikeToPost();
           postRepository.save(post);
           PostLike savedPostLike = postLikeRepository.save(postLike);
           PostLikeResponseDto postLikeResponseDto = PostLikeResponseDto.*builder*()
        	   .postLike(savedPostLike) 
        	   .build();
        	return Optional.of(postLikeResponseDto);
        }
        ```
        
        - 문제가 발생한 코드입니다. `postLikeRepository`에서 좋아요가 존재하면 좋아요가 취소되고, 존재하지 않으면 좋아요가 생기는 로직입니다. 저희는 문제가 발생한 근본적 원인을 **동시성 문제**로 판단하였습니다.
            - **동시성 문제**
                - 사용자 A와 사용자 B가 거의 동시에 동일한 게시물에 대해 `좋아요` 요청을 보냅니다.
                - 첫 번째 요청이 `좋아요` 상태를 확인하고 추가하는 동안, 두 번째 요청도 동일한 작업을 시도합니다.
                - 이로 인해 동일한 `post`와 `user` 조합에 대해 두 개의 `PostLike` 레코드가 생성됩니다.
            - **중복 데이터로 인한 쿼리 오류**
                - 후속 쿼리에서 `post`와 `user` 조건으로 `PostLike`를 조회할 때, 동시성 이슈로 인해 생긴 두 개 이상의 결과가 반환됩니다.
                - 단일 결과만 반환되어야 하는 쿼리가 여러 결과를 반환하면서 `Query did not return a unique result` 오류가 발생합니다.
    - **3. 해결 방법 탐색 및 해결**
        - 해결 방법 탐색
            - **낙관적 락**은 데이터를 수정할 때 다른 트랜잭션이 데이터를 수정했는지를 확인하여, 변경 사항이 있을 경우 예외를 발생시키고 롤백하는 방식입니다.  주로 트랜잭션 충돌이 드문 경우, 데이터의 읽기 작업이 많은 환경, 높은 동시성 요구가 있지만 충돌 가능성이 낮은 경우에 사용합니다.
            - **비관적 락**은 특정 데이터에 대한 접근을 제한하여, 하나의 트랜잭션이 완료될 때까지 다른 트랜잭션이 해당 데이터에 접근하지 못하도록 하는 방식입니다. 주로 트랜잭션 충돌이 자주 발생할 것으로 예상되는 경우, 중요한 데이터의 무결성이 강력하게 요구되는 경우, 동시 접근이 많지 않은 경우에 사용합니다.
            - **분산 락**은 여러 서버가 동일한 자원에 동시에 접근하는 것을 제어하기 위해 설계된 메커니즘입니다. Redis, Zookeeper와 같은 외부 시스템을 이용하여 락을 관리합니다. 주로 분산된 시스템에서 동일한 자원을 보호해야 할 때, 데이터베이스의 인스턴스가 분산되어 있는 경우에 사용합니다.
        - 해결 방법
            - 저희는 단일 서버가 아닌 분산 시스템으로 이루어져 있기 때문에 세 가지 해결 방법 중 분산 락을 선택하여 해당 문제를 해결하였습니다.
- **프론트 예외처리 문제**
    - **1. 문제 상황**
        
        ```java
          @ExceptionHandler({CommonException.class})
          public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(CommonException ex) {
            return ResponseEntity.ok(
                CommonResponse.builder()
                    .response(ex.getResponse())
                    .build());
          }
        ```
        
        - 백엔드에서 예외처리를 commonException의 status코드를   commonResponse로 감싸 data의 status로 내보내서 예외 처리를 하게 로직을 구상하였는데 결국 ok로 감싸져서 리턴 되다 보니 예외처리를 원활하게 하기가 어려웠습니다.
        
    - **2. 원인**
        
        ```java
         ResponseEntity.ok(
                CommonResponse.builder()
                    .response(ex.getResponse())
                    .build());
          }
        ```
        
        - 리턴에 ok로 지정하다보니 프론트에서는 성공으로 처리되어 예외처리가 작동되지 않았습니다.
    - **3. 해결 방법 탐색 및 해결**
        - **해결 방법 탐색**
            - **방법 1:** 프론트에서 응답 값의 데이터에서 응답코드에 따른 예외 로직 따로 작성하기
            - **방법 2:** 백엔드 예외처리 로직을 변경하기
        - 해결
            
            ```java
             @ExceptionHandler({CommonException.class})
              public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(CommonException ex) {
                return ResponseEntity.status(ex.getResponse().getHttpStatus()).body(
                    CommonResponse.builder()
                        .response(ex.getResponse())
                        .build());
              }
            ```
            
            - 백엔드 개발자이기에 1번의 방법보다 2번의 백엔드 로직을 변경하는 방법 선택하였습니다.
            - 처음부터 commonException의 http.status를 받아서 지정하는 방법으로 수정하였고, 테스트 결과 프론트에서 예외 처리가 잘 진행되었습니다.
- **조회수 문제**
    - **1. 문제 상황**
        - 좋아요를 누르면  조회수가 증가하는 문제가 발생하였습니다.
    - **2. 원인**
        - 조회수 중복에 대한 추가적인 검증 로직이 없고, 프론트에서 좋아요를 누르면 게시글 조회 api가 요청이 되는 것이 원인이었습니다.
    - **3. 해결 방법 탐색 및 해결**
        - **해결 방법 탐색**
            - ip : 장소에 따라 유동적으로 변화하며, 근처 여러 기기로 다른 아이디로 접속해도 동일 유저로 식별가능성이 있다.
            - 세션: Stateless 상태의 서버를 유지하기 위해 토큰 방식으로 로그인을 구현했는데 조회수 기능에 세션을 이용하면 토큰을 이용한 이유가 없어지고 세션 양이 많아진다면 서버에 부하가 발생한다.
            - 쿠키: 서버가 아닌 사용자에게 저장되는 것이라 조작과 탈취가  쉽기 때문에 보안에 취약하다
        - **해결**
            - 비회원 사용자도 집계 가능하고,
            - 조회수가 보안이나 서비스적으로 비중이 크지 않으며,
            - db 를 따로 생성하지 않으므로 부담이 적으므로 쿠키를 통해 해결하였습니다.
                
                ```java
                  private void viewCountUp(Post post, HttpServletRequest req, HttpServletResponse res) {
                
                    Cookie oldCookie = null;
                
                    Cookie[] cookies = req.getCookies();
                    if (cookies != null) {
                      for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("postView")) {
                          oldCookie = cookie;
                        }
                      }
                    }
                
                    if (oldCookie != null) {
                      if (!oldCookie.getValue().contains("[" + post.getId() + "]")) {
                        post.addViewToPost();
                        oldCookie.set();
                        res.addCookie(oldCookie);
                      }
                    } else {
                      post.addViewToPost();
                      Cookie newCookie = new Cookie("postView", "[" + post.getId() + "]");
                      newCookie.set();
                      res.addCookie(newCookie);
                    }
                  }
                ```
                
            

---

### 🏖️팀 소개

- 팀원
    
    
    | 이름 | 역할 | 담당 파트 | 개인 깃허브 주소 |
    | --- | --- | --- | --- |
    | 최유진 | 팀장 | - 인증 인가 파트 (로그인, 로그아웃, 토큰 재발급)
    - 유저 파트 (회원가입, 탈퇴, 유저 프로필 조회, 수정)
    - 플랜 파트 (공동 작업자 메일 발송, 인증 확인)
    - 프론트
    - 프론트 서버 배포 | https://github.com/geneeuchoi |
    | 이재성 | 부팀장 | - 플랜 파트 (플랜 CRUD)
    - 칼럼 파트 (칼럼 CRUD)
    - 연관 게시물 파트 (연관 게시물 조회, 포스트에서 카드 생성)
    - 프론트 | https://github.com/Delphinium52 |
    | 이제범 | 팀원 | - 카드 파트 (카드 CRUD) | https://github.com/jebum1019 |
    | 차도범 | 팀원 | - 추천글 파트 (추천글 CRUD, 사진 CRUD, 댓글 CRUD)
    - 좋아요 파트 (추천글 좋아요, 댓글 좋아요)
    - 인프라 설계
    - 프론트
    - 백엔드 서버 배포 | https://github.com/ckehqja |
    
- 협업 방식
    - `자체 컨벤션`을 설정하여 컨벤션에 맞게 코드 작성 및 `깃허브` 관리 [컨벤션 보러 가기](https://www.notion.so/1544fa88bc2a4b8ea52e95e9eeb03fd9?pvs=21)
    - 개발해야 하는 기능, 버그 등을 이슈로 등록하고 개별 브랜치에서 작업하여 PR 요청
    - PR이 올라올 시 모든 팀원이 `코드 리뷰`에 필수 참여하고, 피드백 반영 후 dev 브랜치에 머지
    - `노션`으로 회의록 작성, 일정 관리 [노션 보러가기](https://www.notion.so/2360237586194e21a8e4025d4355a764?pvs=21)
    - `슬랙`으로 실시간 소통, PR 알림
