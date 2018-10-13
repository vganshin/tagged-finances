import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Deposit} from '../../model/deposit.model';
import {BackendService} from '../../services/backend.service';
import {DepositRow} from '../../model/deposit-row.model';

@Component({
  selector: 'app-deposit-list',
  templateUrl: './deposit-list.component.html',
  styleUrls: ['./deposit-list.component.css']
})
export class DepositListComponent implements OnInit {

  newDepositForm: FormGroup;
  newDepositFormVisible: boolean;

  depositRows: DepositRow[];

  constructor(private fb: FormBuilder, private backendService: BackendService) { }

  ngOnInit() {

    this.setupForm();

    this.backendService.getDeposits().subscribe((deposits: Deposit[]) => this.setDeposits(deposits));
  }

  setupForm() {
    this.newDepositForm = this.fb.group({
      name: ['', [
        Validators.required,
        Validators.pattern(/[\wА-я]/)
      ]
      ],
      balance: ['', [
        Validators.required,
        Validators.pattern(/[0-9]+/)
      ]
      ]
    });

    this.newDepositFormVisible = false;
  }

  setDeposits(deposits: Deposit[]) {

    this.depositRows = deposits.map(deposit => {
      return new DepositRow(deposit);
    });

  }

  updateDeposit(depositRow: DepositRow) {
    depositRow.deposit.balance = depositRow.newBalance;
    depositRow.edit = false;
  }

  saveNewDeposit() {
    const controls = this.newDepositForm.controls;
    this.depositRows.push(new DepositRow(new Deposit(controls.name.value, controls.balance.value)));

    this.hideForm();
  }

  showForm() {
    this.newDepositFormVisible = true;
  }

  hideForm() {
    this.newDepositFormVisible = false;
    this.newDepositForm.controls.name.setValue('');
    this.newDepositForm.controls.balance.setValue(0);
  }

  isFormVisible(): boolean {
    return this.newDepositFormVisible;
  }

}
