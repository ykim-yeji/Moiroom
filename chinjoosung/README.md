# 🎈회고록🎈
## 2024.01.15  
여러 논문 자료를 바탕으로 룸메이트 매칭 프로젝트 기획 단계에서 고객이 어떤 부분에 있어서 룸메이트에 흥미를 느낄지 여러 자료 및 설문조사에 근거하여 그 기준을 차근차근 정하게 되었고, 프로젝트의 기능을 어떻게 그려나가야 할지 팀원들과의 회의를 거치면서 방향을 잡을 수 있었습니다.

이 경험을 바탕으로 무조건 어떤 툴이나 기능 등을 다뤄본 적이 없어서 어렵다는 이유로 발뺌하는 것보다는 항상 일단 해보자는 마음가짐을 가지고 해결하겠다는 마음가짐으로 프로젝트를 진행한다면 차근차근 자신감이 생기면서 프로젝트의 길도 차근차근 열릴 것이라는 느낌도 많이 받았습니다.

## 2024.01.16  
erd 테이블의 주소 관련 테이블 내용에서 뭔가 부족한 느낌이 들어 우리나라 주소 행정 체계에 대한 자세한 조사를 진행했습니다.  
우리나라에서는 지번주소 및 도로명주소에서 쓰이는 데이터의 형태가 다르지만 우리 프로젝트에서는 광역시 및 군/구의 데이터만 쓸 것이기 때문에 이를 위한 적절한 주소 명칭 방식은 법정동 입력 방식이 적절하다는 생각이 들었습니다. 이를 바탕으로 테이블을 분할하여 법정동 형식에 맞도록 수정을 하였습니다.  

카카오 소셜 로그인 구현을 위해 카0카오 소셜 로그인 구현 방식에 대한 다양한 자료를 참조하였고, 자료를 바탕으로 실습을 진행을 하려는 도중 database의 사전 설정을 먼저 진행해야 했기에 jpa와 database의 연동 방법을 알아보기 위해서 spring data jpa 문서를 참조하였습니다.  

## 2024.01.17
기능 명세서 검토 과정을 거쳤고, 화면 구성에 대한 중간 점검 회의를 거쳤습니다.
이를 바탕으로 팀미팅에서 다양한 피드백을 받았고 내일부터 피드백 내용을 반영할 생각입니다.  

## 2024.01.18
오늘은 카카오 소셜 로그인 실습과 erd 테이블의 2차 수정을 진행하였습니다.

카카오 서비스를 통해서 회원가입을 진행을 하고, 로그인 또한 진행할 것이기 때문에 이에 대한 적합한 카카오 api를 탐색을 해본 결과 `카카오 싱크` api가 적절하다는 판단을 하였습니다.

그래서 카카오 싱크 서비스를 사용하기 위해 카카오 개발자 페이지에서 저희 서비스를 위한 앱을 생성하였고, 카카오 씽크를 사용하기 위해서는 비즈니스 앱으로 전환할 필요가 있기 때문에 비즈니스 앱으로 전환까지 완료했습니다. 하지만 카카오 씽크를 사용하기 위해서 추가적인 심사 과정을 거쳐야 하는데 최소 소요시간이 3일이 걸리는 것으로 알게 되었습니다. 그래서 카카오 씽크 사용을 위한 심사 신청을 마지막으로 진행을 마쳤습니다.

그리고 erd에서 채팅방 관련 테이블 및 주제 관련 테이블 2차 수정을 마무리하였고, 추가적인 검토를 통해서 계속적인 수정을 진행할 계획입니다.