@formatString = constant [4 x i8] c"%d\0A\00"
declare i32 @printf(i8*, ...)

define i32 @main() {
entry:
	%temp0 = alloca i32, align 4
  %d = shl i32 2, 3
  store i32 120, i32* %temp0, align 4
  %temp1 = load i32, i32* %temp0
  %call = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @formatString , i32 0, i32 0), i32 %temp1)
  ret i32 1
}