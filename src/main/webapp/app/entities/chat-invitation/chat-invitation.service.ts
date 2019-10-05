import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';

type EntityResponseType = HttpResponse<IChatInvitation>;
type EntityArrayResponseType = HttpResponse<IChatInvitation[]>;

@Injectable({ providedIn: 'root' })
export class ChatInvitationService {
  public resourceUrl = SERVER_API_URL + 'api/chat-invitations';

  constructor(protected http: HttpClient) {}

  create(chatInvitation: IChatInvitation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatInvitation);
    return this.http
      .post<IChatInvitation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatInvitation: IChatInvitation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatInvitation);
    return this.http
      .put<IChatInvitation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatInvitation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatInvitation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatInvitation: IChatInvitation): IChatInvitation {
    const copy: IChatInvitation = Object.assign({}, chatInvitation, {
      creationDate:
        chatInvitation.creationDate != null && chatInvitation.creationDate.isValid() ? chatInvitation.creationDate.toJSON() : null,
      acceptanceDenialDate:
        chatInvitation.acceptanceDenialDate != null && chatInvitation.acceptanceDenialDate.isValid()
          ? chatInvitation.acceptanceDenialDate.toJSON()
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
      res.body.acceptanceDenialDate = res.body.acceptanceDenialDate != null ? moment(res.body.acceptanceDenialDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chatInvitation: IChatInvitation) => {
        chatInvitation.creationDate = chatInvitation.creationDate != null ? moment(chatInvitation.creationDate) : null;
        chatInvitation.acceptanceDenialDate =
          chatInvitation.acceptanceDenialDate != null ? moment(chatInvitation.acceptanceDenialDate) : null;
      });
    }
    return res;
  }
}
