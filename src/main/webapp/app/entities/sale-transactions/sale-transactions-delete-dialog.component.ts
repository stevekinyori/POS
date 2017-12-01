import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SaleTransactions } from './sale-transactions.model';
import { SaleTransactionsPopupService } from './sale-transactions-popup.service';
import { SaleTransactionsService } from './sale-transactions.service';

@Component({
    selector: 'jhi-sale-transactions-delete-dialog',
    templateUrl: './sale-transactions-delete-dialog.component.html'
})
export class SaleTransactionsDeleteDialogComponent {

    saleTransactions: SaleTransactions;

    constructor(
        private saleTransactionsService: SaleTransactionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleTransactionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saleTransactionsListModification',
                content: 'Deleted an saleTransactions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-transactions-delete-popup',
    template: ''
})
export class SaleTransactionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleTransactionsPopupService: SaleTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saleTransactionsPopupService
                .open(SaleTransactionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
