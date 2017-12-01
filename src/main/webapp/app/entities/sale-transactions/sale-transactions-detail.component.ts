import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SaleTransactions } from './sale-transactions.model';
import { SaleTransactionsService } from './sale-transactions.service';

@Component({
    selector: 'jhi-sale-transactions-detail',
    templateUrl: './sale-transactions-detail.component.html'
})
export class SaleTransactionsDetailComponent implements OnInit, OnDestroy {

    saleTransactions: SaleTransactions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saleTransactionsService: SaleTransactionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSaleTransactions();
    }

    load(id) {
        this.saleTransactionsService.find(id).subscribe((saleTransactions) => {
            this.saleTransactions = saleTransactions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaleTransactions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'saleTransactionsListModification',
            (response) => this.load(this.saleTransactions.id)
        );
    }
}
