# Databutee

> Databutee는 제 7회 D2 CAMPUS FEST mini의 최우수상 수상작입니다.

Databutee는 분산 인 메모리 키 - 값 스토어 Databute의 클라이언트 구현체입니다.

Databutee의 목표는 Databuter에 연결하여 키 - 값 엔트리를 빠르게 저장하고 조회할 수 있도록 하는 
것입니다. 또한, 클라이언트 사이드 라우팅을 적용하여 분산된 엔트리를 클라이언트가 직접 경로를 찾을 수 
있도록 하는 것입니다.

사용자는 엔트리를 조회 (GET), 저장 (SET, UPDATE) 및 삭제 (DELETE) 하거나 유효 기간을 설정 
(EXPIRE) 할 수 있습니다. 엔트리는 숫자 (Integer, Long), 문자열 (String), 리스트 (List), 셋 
(Set) 및 딕셔너리 (Map) 타입을 지원합니다.

모든 엔트리 연산은 비동기적으로 수행됩니다. 사용자는 엔트리 연산에 콜백을 등록하여, 엔트리 연산이 완료될 
때 콜백에서 그 결과를 처리할 수 있습니다.

모든 엔트리 연산의 라우팅은 클라이언트 사이드에서 수행됩니다. 클라이언트가 먼저 키에 대한 버킷을 계산하여 
그 버킷으로 엔트리 연산을 요청합니다. 라우팅을 위한 통신이 필요 없어 꽤 많은 지연 시간이 단축되며, 키 
계산이 틀리더라도 Databuter는 요청을 올바른 버킷에 전달해 줄 것입니다.

## 사용법

```
gradlew jar
java -jar build/libs/databutee-{version}.jar
```

## 라이센스

```
MIT License

Copyright (c) 2019 databute

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```