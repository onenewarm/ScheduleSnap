#!/usr/bin/env bash

## 이전 배포 파일을 지운다.
#SINCE=`date --date '-4 weeks -2 days' +%F 2>/dev/null || date -v '-4w' -v '-2d' +%F` # day +30일
#bucket=schedulesnapbucket # s3 bucket
#aws s3api list-objects-v2 --bucket $bucket --query 'Contents[?LastModified < `'"$SINCE"'`]' --output  text > file-of-keys # s3에서 최종수정일(LastModified)이 기준으로 오래된 파일 조회하여 파일 목록 생성
#cat file-of-keys | awk '{print $2}' | xargs -P8 -n1000 -L 1 bash -c 'aws s3 rm s3://'$bucket'/$0' # 파일을 읽어와서 | 2번째 컬럼(파일명)만 추려내어 | 1line씩 bash 스크립트 실행 | 실행할 명령은 s3 rm =^ㅅ^=

REPOSITORY=/home/ec2-user/app

echo "> 현재 구동 중인 애플리케이션 pid 확인"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

CURRENT_PID=$(pgrep -f $JAR_NAME)
echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
#-Duser.timezone=Asia/Seoul
nohup java -jar  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &