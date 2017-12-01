import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { GoodsReceivedFromSupplier } from './goods-received-from-supplier.model';
import { GoodsReceivedFromSupplierService } from './goods-received-from-supplier.service';

@Component({
    selector: 'jhi-goods-received-from-supplier-detail',
    templateUrl: './goods-received-from-supplier-detail.component.html'
})
export class GoodsReceivedFromSupplierDetailComponent implements OnInit, OnDestroy {

    goodsReceivedFromSupplier: GoodsReceivedFromSupplier;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private goodsReceivedFromSupplierService: GoodsReceivedFromSupplierService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGoodsReceivedFromSuppliers();
    }

    load(id) {
        this.goodsReceivedFromSupplierService.find(id).subscribe((goodsReceivedFromSupplier) => {
            this.goodsReceivedFromSupplier = goodsReceivedFromSupplier;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGoodsReceivedFromSuppliers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'goodsReceivedFromSupplierListModification',
            (response) => this.load(this.goodsReceivedFromSupplier.id)
        );
    }
}
