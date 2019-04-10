import {Component, ViewChild, Input, SimpleChanges, OnChanges, AfterViewInit} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {animate, state, style, transition, trigger} from '@angular/animations';

import {MatSnackBar} from '@angular/material';

import {merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { Operation } from '../../entities/operations';
import { AccountFormatterPipe } from '../../pipes/account-formatter.pipe';
import {OperationService} from '../../services/operation.service';

/**
 * @title Table retrieving data through HTTP
 */
@Component({
  selector: 'app-data-table',
  styleUrls: ['data-table.component.scss'],
  templateUrl: 'data-table.component.html',
  providers: [AccountFormatterPipe],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})

export class DataTableComponent implements OnChanges, AfterViewInit {

  displayedColumns: string[] = ['amount', 'label', 'date', 'account', 'type'];
  data: Operation[] = [];
  dataSource: MatTableDataSource<Operation>;
  resultsLength = 0;
  isLoadingResults = true;

  expandedElement: Operation | null;

  @Input() requestUrl: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private operationService: OperationService, private snackBar: MatSnackBar) {
    this.dataSource = new MatTableDataSource(this.data);
  }

  ngOnChanges(changes: SimpleChanges) {
    this.requestUrl = changes[`requestUrl`].currentValue;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.ngAfterViewInit();
    this.operationService.sortingUrl = 'toto';
  }

  ngAfterViewInit(): void {
    this.sort.active = 'date';
    this.paginator.pageSize = 5;
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.operationService.getOperations(this.sort.active, this.sort.direction,
            this.paginator.pageIndex, this.paginator.pageSize, this.requestUrl);
        }),
        map(data => {
          this.isLoadingResults = false;
          this.resultsLength = data.totalCount;
          return data.items;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          this.snackBar.open(`The API rate limit has been reached. It will be reset in one minute.`,  'close', {
            duration: 5000,
          });
          return observableOf([]);
        })
      ).subscribe(data => this.data = data);
  }
}
