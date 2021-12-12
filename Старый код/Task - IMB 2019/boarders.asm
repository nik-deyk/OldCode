section .rodata
    _f1_f2 dq __float64__(2.0), __float64__(3.0)
    _f2_f3 dq __float64__(1.0), __float64__(2.0)
    _f1_f3 dq __float64__(0.0001), __float64__(1.0)

section .text
	global f1_f2
f1_f2:
	push ebp
	mov ebp, esp

	finit

	mov eax, dword[ebp + 8]
	cmp eax, 1
	ja .badparam
	jmp .goodparam
  .badparam:
		fldz
		jmp .end
  .goodparam:
	fld qword[_f1_f2 + 8 * eax]

  .end:
	pop ebp
	ret

	global f2_f3
f2_f3:
	push ebp
	mov ebp, esp

	finit

	mov eax, dword[ebp + 8]
	cmp eax, 1
	ja .badparam
	jmp .goodparam
  .badparam:
		fldz
		jmp .end
  .goodparam:
	fld qword[_f2_f3 + 8 * eax]

  .end:
	pop ebp
	ret

	global f1_f3
f1_f3:
	push ebp
	mov ebp, esp

	finit

	mov eax, dword[ebp + 8]
	cmp eax, 1
	ja .badparam
	jmp .goodparam
  .badparam:
		fldz
		jmp .end
  .goodparam:
	fld qword[_f1_f3 + 8 * eax]

  .end:
	pop ebp
	ret