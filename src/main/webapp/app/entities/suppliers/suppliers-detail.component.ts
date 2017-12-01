import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Suppliers } from './suppliers.model';
import { SuppliersService } from './suppliers.service';

@Component({
    selector: 'jhi-suppliers-detail',
    templateUrl: './suppliers-detail.component.html'
})
export class SuppliersDetailComponent implements OnInit, OnDestroy {

    suppliers: Suppliers;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private suppliersService: SuppliersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSuppliers();
    }

    load(id) {
        this.suppliersService.find(id).subscribe((suppliers) => {
            this.suppliers = suppliers;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSuppliers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'suppliersListModification',
            (response) => this.load(this.suppliers.id)
        );
    }
}
