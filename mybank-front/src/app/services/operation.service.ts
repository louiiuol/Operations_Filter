import { Injectable } from '@angular/core';
import { OperationsApi } from '../entities/operations';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class OperationService {

  sortingUrl: string;
  requestUrl: string;
  submitted = false;


  constructor(private http: HttpClient) { }

  createRequest(valueInput: string, fieldInput: string, rangeInput: string) {
    if (typeof valueInput === 'string') {
      if (fieldInput === 'compte') {
        valueInput = valueInput.replace(/-/g, '');
      }
      this.requestUrl = 'http://localhost:8080/operations?' + fieldInput + '=' + valueInput.toLowerCase();
    } else if (fieldInput === 'amount') {
      this.requestUrl = 'http://localhost:8080/operations?' + fieldInput.toLowerCase() + '='
        + valueInput + '&range=' + rangeInput ;
    } else if (fieldInput === 'date'  ) {
      const myDate: Date = valueInput;
      this.requestUrl =  'http://localhost:8080/operations?' + fieldInput.toLowerCase() + '='
        + myDate.toISOString().replace('Z', '' ) + '&range=' + rangeInput ;
    } else {
      this.requestUrl = 'http://localhost:8080/operations?' + fieldInput.toLowerCase() + '=' + valueInput;
    }
    console.log('REQUEST URL ' + this.requestUrl);
    this.submitted = true;
  }

  getOperations(sort: string, order: string, page: number, size: number, url: string): Observable<OperationsApi> {
    this.sortingUrl = `${url}&sort=${sort}&order=${order}&page=${page}&size=${size}`;
    return this.http.get<OperationsApi>(this.sortingUrl);
  }

}
