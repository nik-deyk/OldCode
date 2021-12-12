section .rodata
	a dd 3
	b1 dd 2
	b2 dd 2

section .text
; f1 = exp(−x) + a
	global f1
f1:
	push ebp
	mov ebp, esp

    finit                  
    fldl2e
    fld qword[ebp + 8]  

    fchs
    fmulp     

    fld st0 ; dublicate
    fld1
    fxch st1
    fprem1 ; st0 = remaining of division st0/st1
    fxch st1

    ffree st0
    fincstp
    
	fxch st1
    fsub st0, st1 ; explicitly specify the parameters
    frndint ; not obligatory
    fld1
    fscale ; st0 = st0 * 2^st1
    fxch st1
    ffree st0
    fincstp
    fxch st1
    f2xm1 ; st0 = 2^st0 - 1
    fld1
    faddp
    fmulp
    fild dword[a]
    faddp

	pop ebp
	ret

; f2 = b1*x – b2
	global f2
f2:
	push ebp
	mov ebp, esp

	finit
	fld qword[ebp + 8]
	fild dword[b1]
	fmulp

	fild dword[b2]
	fsubp

	pop ebp
	ret

; f3 = 1/x
	global f3
f3: 
	push ebp
	mov ebp, esp

	finit
	fld qword[ebp + 8]
	fld1
	fdivrp

	pop ebp
	ret