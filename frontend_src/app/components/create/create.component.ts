import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  transactionForm: FormGroup;
  items: FormArray;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {

    this.transactionForm = this.fb.group({
      name: ['', [
        Validators.required,
        Validators.pattern(/[А-я]/)
      ]
      ],
      name1: ['', [
        Validators.required, Validators.email
      ]
      ],
      name2: ['Иван2'],
      name3: ['Иван3'],
      items: this.fb.array([ this.createItem() ])
    });


  }

  addItem(): void {
    this.items = this.transactionForm.get('items') as FormArray;
    this.items.push(this.createItem());
  }

  createItem(): FormGroup {
    return this.fb.group({
      name: '',
      description: ''
    });
  }

  isControlValid(controlName: string): boolean {
    const control = this.transactionForm.controls[controlName];
    return control.valid;
  }

  onSubmit() {
    const controls = this.transactionForm.controls;

    /** Проверяем форму на валидность */
    if (this.transactionForm.invalid) {
      /** Если форма не валидна, то помечаем все контролы как touched*/
      Object.keys(controls)
        .forEach(controlName => controls[controlName].markAsTouched());

      /** Прерываем выполнение метода*/
      return;
    }

    /** TODO: Обработка данных формы */
    console.log(this.transactionForm.value);
  }

}
