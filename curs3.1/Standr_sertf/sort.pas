
Procedure BubbleSort(numbers : Array of Integer; size : Integer);
Var
  i, j, temp : Integer;
Begin
  For i := 0 To size-1 do
    For j := 0 to size-i-1 do
      If (numbers[j-1] > numbers[j]) Then
      Begin
        temp := numbers[j-1];
        numbers[j-1] := numbers[j];
        numbers[j] := temp;
      End;
End.