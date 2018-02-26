boolean sit = false;  //앉은지 안앉았는지

void setup(){
  Serial.begin(9600);   //연결
  
}
 
void loop(){
  int sensor0 = analogRead(A0); 
  int sensor1 = analogRead(A1); 
  int sensor2 = analogRead(A2); 
  int sensor3 = analogRead(A3); 

 
  //int press1 = map(pressure, 0, 1024, 0, 255); //0~1024의 센서값을 0~255로 변환
  //Serial.print("sensor0 : ");
  Serial.println(sensor0);
  //Serial.print("sensor1 : ");
  Serial.println(sensor1);
  //Serial.print("sensor2 : ");
  Serial.println(sensor2);
  //Serial.print("sensor3 : ");
  Serial.println(sensor3);
  //Serial.println("------------------");
  
  //앉아있을 경우
  /*
   if (pressure > 100) {
    Serial.println("sitdown");
    //rtc.startRTC(); //rtc시작(시간 측정)
    sit = true; //앉아있는 상태로 변경
  }
  else
  {
    sit=false;
  }*/
  delay(10000); 
}
