import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Packaging } from './packaging.model';
import { PackagingService } from './packaging.service';

@Component({
    selector: 'jhi-packaging-detail',
    templateUrl: './packaging-detail.component.html'
})
export class PackagingDetailComponent implements OnInit, OnDestroy {

    packaging: Packaging;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private packagingService: PackagingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPackagings();
    }

    load(id) {
        this.packagingService.find(id).subscribe((packaging) => {
            this.packaging = packaging;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPackagings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'packagingListModification',
            (response) => this.load(this.packaging.id)
        );
    }
}
