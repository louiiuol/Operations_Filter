import { Component } from '@angular/core';
import { OperationType } from '../../entities/operations';

import { AccountFormatterPipe } from '../../pipes/account-formatter.pipe';
import {OperationService} from '../../services/operation.service';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.scss'],
  providers: [AccountFormatterPipe],
})
export class SearchFormComponent {

  fieldInput = '';
  valueInput: string;
  rangeInput: string;
  type = OperationType;

  constructor(private operationService: OperationService) { }

  getTypes(): Array<string> {
    const keys = Object.keys(this.type);
    return keys.slice(keys.length / 2);
  }
  selectChangeHandler(event: any) {
    this.fieldInput = event.target.value;
  }
  searchOperation() {
    this.operationService.createRequest(this.valueInput, this.fieldInput, this.rangeInput);
  }
}
