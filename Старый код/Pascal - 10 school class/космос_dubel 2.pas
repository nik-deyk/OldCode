Program kosmos_dubl_2;                              //n - кол-во космотел
uses ABCObjects, GraphABC;                      //vp - макс начальная скорость планеты
Const n=15; vp=3; vz=3; koff=2.6; step=10; //vz - макс нач скор звезды (черные дыры на начало v = 0)
type arr=array[1..n] of ObjectABC;              //koff - коэфицент: при его увеличен, сила тяги уменьшаеся 5.6
var kos:arr; dx, dy:longint; par:boolean;       //step - время (милсек) между циклами

        //заполнение (процедура)

procedure zapolni(var a:arr; n:integer);
   begin
       Window.Maximize;  
       Window.Caption:='Модель Космоса в ПАСКАЛЬ';
       window.IsFixedSize:=True;
       for var i:=1 to n do
           begin
                var k:=Random(31)+1;
                       case k of
                       1..15:begin
                                      a[i]:= new CircleABC(Random(100, window.Width-100), Random(100, window.height-100), random(5, 10), clrandom);
                                      while a[i].color=clblack do a[i].Color:=clrandom;
                                      a[i].FontColor:=clred; ///планета
                                      a[i].dx:=random(-vp, vp);
                                      a[i].dy:=random(-vp, vp);
                                end;
                       16..30:begin
                                      a[i]:= new StarABC(Random(100, window.Width-100), Random(100, window.height-100), random(10, 15), random(3, 6), 4, clBlue);
                                      a[i].FontColor:=clblue; ///звезда
                                      a[i].dx:=random(-vz, vz);
                                      a[i].dy:=random(-vz, vz);
                                  end;
                       31: begin
                                      a[i]:= new CircleABC(Random(100, window.Width-100), Random(100, window.height-100), random(7, 14), clblack);
                                      a[i].FontColor:=clblack; ///черная дыра :-)
                             end;
                       end; 
             end;
 end;
    
    //масса тела (функция)
    
function mas(p:ObjectABC):integer;
 begin
 if p.FontColor=clred then Result:=4;
 if p.FontColor=clblue then Result:=7;
 if p.FontColor=clblack then Result:=15;
 end; 
    
   //нахождение равнодействующей и изменение вектора скорости (процедура)
   
procedure ravnod(var dx, dy:integer; x1, y1, i: integer);
var kx, ky, sila, x, y:real;
  begin
      kx:=dx; ky:=dy;
      for var k:=0 to Objects.Count-1 do
              if (k<>i)and(Objects[k].FontColor<>clgreen) then  
                begin
                      x:=Objects[k].Left-x1;
                      y:=-y1+Objects[k].top;
                      sila:=6.67*mas(Objects[k])*10/(koff*(x*x+y*y))*60;
                      if x<>0 then begin
                                                                    if ((x>0) and (y>0))or((x>0) and (y<0)) then begin
                                                                    kx += cos(arctan(y/x))*sila;
                                                                    ky += sin(arctan(y/x))*sila; end;
                                                                    if ((x<0) and (y>0))or((x<0) and (y<0)) then begin
                                                                    kx -= cos(arctan(y/x))*sila;
                                                                    ky -= sin(arctan(y/x))*sila; end;
                                            end else
                      if y>0 then ky += sila else ky -= sila;
                end;
      dx:=round(kx);
      dy:=round(ky);
  end;
  
  //столкновение (процедура)
  
procedure boom(a, b:integer);
var f:arr;
  Begin
     if (Objects[a].FontColor=clblack)and(Objects[b].FontColor<>clblack) then begin Objects[b].destroy; exit; end;
     if (Objects[b].FontColor=clblack)and(Objects[a].FontColor<>clblack) then begin Objects[a].destroy; exit; end;
     if (Objects[a].FontColor=clred)and(Objects[b].FontColor=clred) then 
          begin
               for var u:=1 to 7 do 
                        begin
               f[u]:=new SquareABC(random(Objects[a].Left-40, Objects[b].Left+40), random(Objects[a].top-40, Objects[b].top+40), 3, clred); 
               f[u].FontColor:=clgreen; 
                            end;
               Objects[a].destroy;
               Objects[b].destroy; 
               exit;
          end;
      if (Objects[a].FontColor=clblue)and(Objects[b].FontColor<>clblue) then begin Objects[b].destroy; exit; end;
      if (Objects[b].FontColor=clblue)and(Objects[a].FontColor<>clblue) then begin Objects[a].destroy; exit; end;
      if (Objects[a].FontColor=clblue)and(Objects[b].FontColor=clblue) then  begin
               Objects[a].destroy;
               Objects[b].destroy; 
               exit;
          end;
      if (Objects[a].FontColor=clgreen) then Objects[a].destroy;
      if (Objects[b].FontColor=clgreen) then Objects[b].destroy;
  end;
  
  //сама программа:
  
BEGIN
   zapolni(kos, n);
   LockDrawingObjects;
   while Objects.Count<>0 do 
      begin
            for var j:=0 to Objects.Count-1 do      
                begin
                     dx:=Objects[j].dx;
                     dy:=Objects[j].dy;
                     ravnod(dx, dy, Objects[j].left, Objects[j].top, j);
                     //определяем, выходит ли космотело за границы экрана
                     if (Objects[j].top+dy+20>Window.Height)or(Objects[j].top<0)or(Objects[j].Left+dx+20>Window.Width)or(Objects[j].Left<0) then 
                          begin
                               Objects[j].Destroy; break;
                          end 
                      //если не выходит, то меняем векторы скорости и перемещаем объект
                     else 
                          begin
                               Objects[j].dy:=dy;
                               Objects[j].dx:=dx;
                               Objects[j].Move;
                          end;
                      //проверка на столкновение
                      par:=false;
                      for var r:=0 to Objects.Count-1 do 
                          if (Objects[j].Bounds.IntersectsWith(Objects[r].Bounds))and(r<>j) then
                               begin
                                  if r<>0 then boom(j, r-1) else boom(r, j-1); 
                                     par:=true; 
                                     break;
                               end;
                      if par then break;
                end; 
                   redrawObjects; 
                   sleep(step);
      end;
sleep(2000);
Window.Close;
END.