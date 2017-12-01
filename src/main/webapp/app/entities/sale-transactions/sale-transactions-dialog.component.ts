import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleTransactions } from './sale-transactions.model';
import { SaleTransactionsPopupService } from './sale-transactions-popup.service';
import { SaleTransactionsService } from './sale-transactions.service';
import { Inventory, InventoryService } from '../inventory';
import { Sales, SalesService } from '../sales';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sale-transactions-dialog',
    templateUrl: './sale-transactions-dialog.component.html'
})
export class SaleTransactionsDialogComponent implements OnInit {

    saleTransactions: SaleTransactions;
    isSaving: boolean;

    inventories: Inventory[];

    sales: Sales[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saleTransactionsService: SaleTransactionsService,
        private inventoryService: InventoryService,
        private salesService: SalesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.inventoryService.query()
            .subscribe((res: ResponseWrapper) => { this.inventories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.salesService.query()
            .subscribe((res: ResponseWrapper) => { this.sales = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.saleTransactions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saleTransactionsService.update(this.saleTransactions));
        } else {
            this.subscribeToSaveResponse(
                this.saleTransactionsService.create(this.saleTransactions));
        }
    }

    private subscribeToSaveResponse(result: Observable<SaleTransactions>) {
        result.subscribe((res: SaleTransactions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SaleTransactions) {
        this.eventManager.broadcast({ name: 'saleTransactionsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackInventoryById(index: number, item: Inventory) {
        return item.id;
    }

    trackSalesById(index: number, item: Sales) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sale-transactions-popup',
    template: ''
})
export class SaleTransactionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleTransactionsPopupService: SaleTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saleTransactionsPopupService
                    .open(SaleTransactionsDialogComponent as Component, params['id']);
            } else {
                this.saleTransactionsPopupService
                    .open(SaleTransactionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
