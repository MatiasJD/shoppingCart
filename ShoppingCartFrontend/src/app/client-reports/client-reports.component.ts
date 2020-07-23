import {Component, Inject, OnInit} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {ClientReportsService} from '../service/client-reports.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {formatDate} from '@angular/common';

@Component({
  selector: 'app-client-reports',
  templateUrl: './client-reports.component.html',
  styleUrls: ['./client-reports.component.css']
})
export class ClientReportsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'surname', 'email', 'vipStartDate', 'vipEndDate', 'vip'];
  clients:[];
  message: string;

  constructor(private clientReportsService: ClientReportsService, private dialog: MatDialog) { }

  private dateInicioVip;
  private dateFinVip;

  ngOnInit(): void {
  }

  openDialog() {
    this.dialog.open(DialogElementClient, {
      width: '50%',
      data: {message: this.message}
    });
  }

  changeDateInicioVip(event: MatDatepickerInputEvent<Date>){
    console.log(event);
    this.dateInicioVip = event.value.toLocaleDateString();
    console.log(event.value.toLocaleDateString());
  }

  changeDateFinVip(event: MatDatepickerInputEvent<Date>){
    console.log(event);
    this.dateFinVip = event.value.toLocaleDateString();
    console.log(event.value.toLocaleDateString());
  }

  buscarClientesVip() {
    this.clientReportsService.getClientesVip().subscribe(clients => {
      this.clients = clients;
    }, error => {
      this.message = error.error.message;
      this.openDialog();
    });
  }


  buscarClientesNoVipPorMes(){
    this.dateFinVip = formatDate(this.dateFinVip, 'yyyy-MM-dd', 'en_US');
    this.clientReportsService.getClientesNoVipPorMes(this.dateFinVip).subscribe(clients => {
      this.clients = clients;
    }, error => {
      this.message = error.error.message;
      this.openDialog();
    });
  }

  buscarClientesVipPorMes(){
    this.dateInicioVip = formatDate(this.dateInicioVip, 'yyyy-MM-dd', 'en_US');
    this.clientReportsService.getClientesVipPorMes(this.dateInicioVip).subscribe(clients => {
      this.clients = clients;
    }, error => {
      this.message = error.error.message;
      this.openDialog();
    });
  }
}

@Component({
  selector: 'dialog-element-client',
  templateUrl: 'dialog-element-client.html',
})
export class DialogElementClient {

  constructor(
    public dialogRef: MatDialogRef<DialogElementClient>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  close(): void {
    this.dialogRef.close();
  }
}
